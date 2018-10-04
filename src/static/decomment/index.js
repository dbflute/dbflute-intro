import ApiClient from 'api-client'

/**
 * @author deco
 * @author cabos
 */
function Decomment() {
  this.storeClient = new StoreClient()
}

Decomment.prototype = {

  /**
   * init
   */
  init: function () {
    if (document.getElementById("intro_opening") !== null) {
      this.fetchVirtualPickup();
      this.setupKeyDownEvent();
      this.showUserNameInputIfNeed();
    } else {
      this.reflectEmbeddedPickup();
    }
  },

  /**
   * Fetch virtual pickup from server
   */
  fetchVirtualPickup: function () {
    var that = this;
    new ApiClient().fetchDecomment(function (response) {
      var decomment = JSON.parse(response);
      that.reflectPickup(decomment);
      that.setupSaveDecomment();
    })
  },

  /**
   * Show user name input when decomment server mode.
   */
  showUserNameInputIfNeed: function () {
    if (DecommentUtil.isDecommentServer() && this.isUserNameEmpty()) {
      document.querySelector('#decomment-user-name').style.display = 'block';
    }
  },

  hideUserNameInput: function () {
    document.querySelector('#decomment-user-name').style.display = 'none';
  },

  /**
   * Reflect embedded pick
   */
  reflectEmbeddedPickup: function () {
    this.reflectPickup(this.storeClient.embeddedPickup);
  },

  /**
   * Reflect pickup
   * @param {Object} decomment - Decomment object
   */
  reflectPickup: function (decomment) {
    var that = this;
    this.storeClient.unmappedTableArea.clear();
    this.storeClient.unmappedColumnArea.clear();
    this.storeClient.storePickup(decomment);
    var pickup = this.storeClient.getDecomments()
    Array.prototype.forEach.call(pickup, function (table) {
      table.reflectToHtml();
      for (var key in table.columns) {
        var column = table.columns[key]
        column.reflectToHtml();
      }
    });
    this.storeClient.unmappedTableArea.reflect();
    this.storeClient.unmappedColumnArea.reflect();
  },

  /**
   * Setup save decomment event
   * @param {string} table - Table name
   */
  setupSaveDecomment: function (schemeName) {
    this.setupSaveTableComment();
    this.setupSaveColumnComment();
  },

  /**
   * Setup save table comment event
   */
  setupSaveTableComment: function () {
    var that = this;
    var tableList = document.getElementById('table-list-body').querySelectorAll('tr');
    Array.prototype.forEach.call(tableList, function (table) {
      var tableName = table.querySelector('.namecell').getAttribute('data-name');
      var commentElement = table.querySelector('.commentcell');
      that.setupSaveTableClickEvent(tableName, commentElement);
    });
    var commentTables = document.querySelectorAll('.tablecomment')
    Array.prototype.forEach.call(commentTables, function (tableElement) {
      var tableName = tableElement.getAttribute('data-name');
      that.setupSaveTableClickEvent(tableName, tableElement);
    });
  },

  /**
   * Setup save table comment click event to html
   * @param {string} tableName - Table name
   * @param {Object} commentElement - Comment element
   */
  setupSaveTableClickEvent: function (tableName, commentElement) {
    var tableProperty = this.storeClient.getTablePickup(tableName) || new PickupTableProperty(tableName, null)
    commentElement.onclick = this.createSaveDecommentFunction(tableProperty);
  },

  /**
   * Setup save column comment event
   */
  setupSaveColumnComment: function () {
    var tables = document.getElementsByClassName('tablecontent');
    var that = this;
    Array.prototype.forEach.call(tables, function (table) {
      var columns = table.getElementsByTagName('tr');
      Array.prototype.forEach.call(columns, function (column, index) {
        if (index === 0) { // because table header
          return
        }

        var commentElement = column.querySelector('.commentcell');
        var tableName = that.getTableNameFromElement(table);
        var columnName = column.querySelector('.columnnamecell').innerText;
        var columnProperty = that.storeClient.getColumnPickup(tableName, columnName) || new PickupTableProperty(tableName, columnName)
        commentElement.onclick = that.createSaveDecommentFunction(columnProperty);
      });
    });
  },

  /**
   * Create save decomment function
   * param {object} commentProperty - Comment property
   * returns {function} Save decomment function
   */
  createSaveDecommentFunction: function (commentProperty) {
    var that = this;
    return function () {
      var modalElement = document.getElementById("decomment-modal");
      modalElement.style.display = "block";

      document.getElementById("decomment-modal-column-info").innerText = commentProperty.toDialogTitle();

      var inputElement = document.getElementById("decomment-input");
      var previousWholeComment = commentProperty.getDisplayHtmlComment();
      inputElement.value = previousWholeComment;
      inputElement.focus();

      document.getElementById("decomment-close").onclick = function () {
        modalElement.style.display = "none";
      };

      document.getElementById("decomment-ok").onclick = function () {
        var decomment = inputElement.value;
        if (DecommentUtil.isDecommentServer() && that.isUserNameEmpty()) {
          alert('Input your user name.');
          return;
        }
        if (that.isStillConflicting(decomment)) {
          alert('Still conflicting. please resolve.')
          return;
        }
        if (!that.isCommentChanged(previousWholeComment, decomment)) {
          modalElement.style.display = "none";
          return;
        }
        decomment = DecommentUtil.escapeInputText(decomment);
        decomment = DecommentUtil.deleteUnnecessaryWhitespace(decomment);
        commentProperty.decomments = [{'body': decomment}]
        commentProperty.inputAuthor = that.getUserName();
        var params = commentProperty.toRequestParams()
        new ApiClient().saveDecomment(params, function () {
          that.fetchVirtualPickup();
          that.hideUserNameInput();
        });
        modalElement.style.display = "none";
      };

      window.onclick = function (event) {
        if (event.target == modalElement) {
          modalElement.style.display = "none";
        }
      };
    };
  },

  /**
   * Get user name. and save to cookie if not saved.
   */
  getUserName: function () {
    var savedUserName = this.getCookie('decomment-user-name');
    if (savedUserName) {
      return savedUserName;
    }
    var inputUserName = document.querySelector('#decomment-user-name').value;
    if (inputUserName && inputUserName.length > 0) {
      document.cookie = "decomment-user-name=" + inputUserName + ";expires=2147483647";
    }
    return inputUserName;
  },

  /**
   * Check user name is inputed.
   */
  isUserNameEmpty: function () {
    return this.getCookie('decomment-user-name') === null && this.getUserName().length === 0;
  },

  /**
   * Get cookie from key
   */
  getCookie: function (key) {
    var cookies = document.cookie.split('; ');
    for (var i = 0; i < cookies.length; i++) {
      var c = cookies[i].split('=');
      if (c[0] === key) {
        return c[1];
      }
    }
    return null;
  },

  /**
   * Setup key down event
   */
  setupKeyDownEvent: function () {
    document.onkeydown = function (e) {
      // ESC
      if (e.keyCode == 27) {
        var modalElement = document.getElementById("decomment-modal");
        modalElement.style.display = "none";
      }
    }
  },

  /**
   * Get table name
   * @param {Object} table - Table element
   * @returns {string} table name
   */
  getTableNameFromElement: function (table) {
    var previousElement = table.previousElementSibling;
    return previousElement.value;
  },

  /**
   * Check still conflicting
   * @param {string} decomment - decomment
   * @returns {Boolean} is still conflicting
   */
  isStillConflicting: function (decomment) {
    return decomment.includes('=======');
  },

  /**
   * Check new decomment and previous comment is different
   * @param {string} tableName - Table name
   * @param {string} columnName - Column name
   * @param {string} decomment - decomment
   * @returns {Boolean} is changed
   */
  isCommentChanged: function (previousWholeComment, decomment) {
    if (previousWholeComment) {
      return previousWholeComment.trim() !== decomment.trim();
    }
    return decomment.trim().length != 0;
  }
};

/**
 * @author cabos
 */
function UnmappedColumnRow() {
}

UnmappedColumnRow.prototype = {

  /**
   * Initialize unmapped column row
   * @param {number} Row number
   * @param {string} Old table name
   */
  init: function (rowNum, table, column) {
    this.rowNum = rowNum;
    this.tableName = table.tableName;
    this.oldColumnName = column.columnName;

    this.mappedColumnNameList = Array.prototype.map.call(column.mappings, function (mapping) {
      return mapping.newTableName + '.' + mapping.newColumnName;
    });
    this.previousMappingList = Array.prototype.map.call(column.mappings, function (mapping) {
      return mapping.mappingCode;
    });
    this.authorList = Array.prototype.map.call(column.mappings, function (mapping) {
      return mapping.mappingOwner;
    });
  },

  /**
   * Convert unmapped column row to string
   * @returns {string} Unmapped column row
   */
  toDisplayHtmlString: function () {
    return '<tr id="unmapped-column-row-' + this.oldColumnName + '">'
      + '<td class="rownumcell">' + this.rowNum + '</td>'
      + '<td class="namecell">' + this.tableName + '</td>'
      + '<td class="namecell">' + this.oldColumnName + '</td>'
      + '<td class="namecell columnconflictcell">' + this.toConflictDisplayHtmlString() + '</td>'
      + '<td id="columnselectorcell-' + this.tableName + '-' + this.oldColumnName + '">' + '</td>'
      + '</tr>';
  },

  toConflictDisplayHtmlString: function () {
    return Array.prototype.reduce.call(this.mappedColumnNameList, function (currentStrs, name) {
      if (currentStrs.length === 0) {
        return name;
      }
      return currentStrs + "<br>" + name;
    }, '');
  },

  isColumnNameConflict: function () {
    return this.previousMappingList.length >= 2;
  }
}

#
#--
---------------------------------------------------
  #
#                        Decomment
Unmapped
Column
Area
#
#--
----------------------------
  /**
   * @author cabos
   */
  function UnmappedColumnArea() {
    this.rows = [];
  }
UnmappedColumnArea.prototype = {

  /**
   * Push unmapped column
   */
  push: function (table, column) {
    var row = new UnmappedColumnRow();
    row.init(this.rows.length + 1, table, column);
    this.rows.push(row);
  },

  /**
   * Activate unmapped column field
   */
  activate: function () {
    if (document.getElementById("intro_opening") === null) {
      return;
    }

    var unmappedElements = document.getElementById('unmapped-columns');
    unmappedElements.style.display = 'inline';
    var that = this;
    document.getElementById('column-mapping-submit-button').onclick = function () {
      that.postMappingColumn();
    };
  },

  /**
   * Clear unmapped column field
   */
  clear: function () {
    var tbody = this.getColumnBodyElement();
    this.rows = [];
    tbody.innerHTML = '';

    var unmappedElements = document.getElementById('unmapped-columns');
    unmappedElements.style.display = 'none';
  },

  /**
   * Reflect unmapped column field
   */
  reflect: function () {
    if (this.rows.length === 0) {
      return;
    }

    this.activate();

    var tbody = this.getColumnBodyElement();
    var that = this;
    Array.prototype.forEach.call(this.rows, function (row) {
      tbody.innerHTML += row.toDisplayHtmlString();
      that.displayColumnSelector(row.tableName, row.oldColumnName);
    });

    if (!this.isColumnNameConflict()) {
      document.querySelector('#unmapped-column-head th.conflictcell').style.display = 'none';
      Array.prototype.forEach.call(document.getElementsByClassName('columnconflictcell'), function (cell) {
        cell.style.display = 'none'
      });
    }
  },

  isColumnNameConflict: function () {
    return Array.prototype.some.call(this.rows, function (row) {
      return row.isColumnNameConflict();
    });
  },

  /**
   * Display column selector
   */
  displayColumnSelector: function (tableName, oldColumnName) {
    var columnSelector = this.createColumnSelector(tableName);
    var columnSelectorCell = document.getElementById('columnselectorcell-' + tableName + '-' + oldColumnName);
    columnSelectorCell.innerHTML = columnSelector;
  },

  /**
   * Create select list of column
   *  <select>
   *    <option>COLUMN_NAME1</option>
   *    <option>COLUMN_NAME2</option>
   *    <option>COLUMN_NAME3</option>
   *    ...
   *  </select>
   * @returns {string} Column selector
   */
  createColumnSelector: function (tableName) {
    var columnNameList = this.getColumnNameList(tableName);
    var options = Array.prototype.reduce.call(columnNameList, function (currentOptions, name) {
      return currentOptions + '<option>' + name + '</option>';
    }, '');
    return '<select>' + '<option>-</option>' + options + '</select>'
  },

  /**
   * Get column name list by table name
   * @returns {array} column name list
   */
  getColumnNameList: function (tableName) {
    var trs = document.getElementById(tableName.toLowerCase() + '-body').getElementsByTagName('tr');
    var columnList = [];
    for (var i = 0, max = trs.length; i < max; i++) {
      // TODO check that column name column num is 6
      #if($isAliasDelimiterInDbCommentValid)
      columnList.push(trs[i].getElementsByTagName('td')[7].innerText);
      #else
      columnList.push(trs[i].getElementsByTagName('td')[6].innerText);
      #end
    }
    return columnList;
  },

  /**
   * Get Unmapped column body element
   * @returns {object} Unmapped column body element
   */
  getColumnBodyElement: function () {
    return document.getElementById('unmapped-column-body');
  },

  /**
   * Convert to string
   *  <tr>
   *    <td class="rownumcell">1</td>
   *    <td class="namecell">TABLE_NAME1</td>
   *    <td class="namecell">OLD_COLUMN_NAME1</td>
   *    <td class="columnselectorcell">
   *    </td>
   *  </tr>
   *  <tr>
   *    <td class="rownumcell">1</td>
   *    <td class="namecell">TABLE_NAME2</td>
   *    <td class="namecell">OLD_COLUMN_NAME2</td>
   *    <td class="columnselectorcell">
   *    </td>
   *  </tr>
   *  ....
   *  @returns {string} Unmapped column rows
   */
  toDisplayHtmlString: function () {
    return Array.prototype.reduce.call(this.rows, function (currentRows, row) {
      return currentRows + row.toDisplayHtmlString();
    }, '');
  },

  postMappingColumn: function () {
    var mappings = Array.prototype.map.call(this.rows, function (row) {
      var tableName = row.tableName;
      var oldColumnName = row.oldColumnName;
      var selectorCellId = 'columnselectorcell-' + tableName + '-' + oldColumnName;
      var newColumnName = document.getElementById(selectorCellId).children[0].value;
      return {
        oldTableName: tableName,
        oldColumnName: oldColumnName,
        newTableName: tableName,
        newColumnName: newColumnName,
        targetType: 'COLUMN',
        authors: row.authorList,
        previousMappings: row.previousMappingList
      };
    });

    mappings = Array.prototype.filter.call(mappings, function (mapping) {
      return mapping.newColumnName !== '-';
    });

    if (mappings.length === 0) {
      return;
    }

    var params = {mappings: mappings};
    var successCallback = function () {
      window.location.reload();
    };
    new ApiClient().postMapping(params, successCallback);
  }
}
new Decomment().init();
