/**
 * @author cabos
 */
export class UnmappedTableArea {
  constructor() {
    this.rows = [];
  }

  /**
   * Push unmapped table
   */
  push(table) {
    var row = new UnmappedTableRow();
    row.init(this.rows.length + 1, table);
    this.rows.push(row);
  }

  /**
   * Activate unmapped table field
   */
  activate() {
    if (document.getElementById("intro_opening") === null) {
      return;
    }

    var unmappedElements = document.getElementById('unmapped-tables');
    unmappedElements.style.display = 'inline';
    var that = this;
    document.getElementById('table-mapping-submit-button').onclick = function () {
      that.postMappingTable();
    };
  }

  /**
   * Clear unmapped table field
   */
  clear() {
    var tbody = this.getTableBodyElement();
    tbody.innerHTML = '';
    this.rows = [];

    var unmappedElements = document.getElementById('unmapped-tables');
    unmappedElements.style.display = 'none';
  }

  /**
   * Reflect unmapped table body
   */
  reflect() {
    if (this.rows.length === 0) {
      return;
    }

    this.activate();

    var tbody = this.getTableBodyElement();
    tbody.innerHTML += this.toDisplayHtmlString();
    this.showTableSelector();

    if (!this.isTableNameConflict()) {
      document.querySelector('#unmapped-table-head th.conflictcell').style.display = 'none';
      Array.prototype.forEach.call(document.getElementsByClassName('tableconflictcell'), function (cell) {
        cell.style.display = 'none'
      });
    }
  }

  isTableNameConflict() {
    return Array.prototype.some.call(this.rows, function (row) {
      return row.isTableNameConflict();
    });
  }

  /**
   * Show table selector
   *  <select>
   *    <option>TABLE_NAME1</option>
   *    <option>TABLE_NAME2</option>
   *    <option>TABLE_NAME3</option>
   *    ...
   *  </select>
   * @returns {string} show table selector
   */
  showTableSelector() {
    var tableSelector = this.createTableSelector();
    var tableSelectorCellList = document.getElementsByClassName('tableselectorcell');
    Array.prototype.forEach.call(tableSelectorCellList, function (cell) {
      cell.innerHTML = tableSelector;
    });
  }

  /**
   * Create select list of table
   * @returns {string}
   */
  createTableSelector() {
    var tableNameList = this.getTableNameList();
    var options = Array.prototype.reduce.call(tableNameList, function (currentOptions, name) {
      return currentOptions + '<option>' + name + '</option>';
    }, '');
    return '<select>' + '<option>-</option>' + options + '</select>'
  }

  /**
   * Get data list
   * @returns {array} table name list
   */
  getTableNameList() {
    var trs = document.getElementById('table-list-body').getElementsByTagName('tr');
    var tableList = [];
    for (var i = 0, max = trs.length; i < max; i++) {
      tableList.push(trs[i].getElementsByTagName('td')[NAME_INDEX].innerText);
    }
    return tableList;
  }

  /**
   * Get Unmapped table body element
   * @returns {object} Unmapped table body element
   */
  getTableBodyElement() {
    return document.getElementById('unmapped-table-body');
  }

  /**
   * Convert to string
   *  <tr>
   *    <td class="rownumcell">1</td>
   *    <td class="namecell">OLD_TABLE_NAME1</td>
   *    <td class="columnselectorcell">
   *    </td>
   *  </tr>
   *  <tr>
   *    <td class="rownumcell">1</td>
   *    <td class="namecell">OLD_TABLE_NAME2</td>
   *    <td class="columnselectorcell">
   *    </td>
   *  </tr>
   *  ....
   * @returns {string} Unmapped table rows
   */
  toDisplayHtmlString() {
    return Array.prototype.reduce.call(this.rows, function (currentRows, row) {
      return currentRows + row.toDisplayHtmlString();
    }, '');
  }

  postMappingTable() {
    var mappings = Array.prototype.map.call(this.rows, function (row) {
      var newTableName = document.getElementById('unmapped-table-row-' + row.tableName).children[3].children[0].value;
      return {
        oldTableName: row.tableName,
        newTableName: newTableName,
        targetType: 'TABLE',
        authors: row.authorList,
        previousMappings: row.previousMappingList
      };
    });

    mappings = Array.prototype.filter.call(mappings, function (mapping) {
      return mapping.newTableName !== '-';
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

class UnmappedTableRow {

  /**
   * Initialize unmapped table row
   * @param {number} row number
   * @param {object} table
   */
  init(rowNum, table) {
    this.rowNum = rowNum;
    this.tableName = table.tableName;
    this.mappedTableNameList = Array.prototype.map.call(table.mappings, function (mapping) {
      return mapping.newTableName;
    });
    this.previousMappingList = Array.prototype.map.call(table.mappings, function (mapping) {
      return mapping.mappingCode;
    });
    this.authorList = Array.prototype.map.call(table.mappings, function (mapping) {
      return mapping.mappingOwner;
    });
  }

  /**
   * Convert unmapped table row to string
   * @returns {string} Unmapped table row
   */
  toDisplayHtmlString() {
    return '<tr id="unmapped-table-row-' + this.tableName + '">'
      + '<td class="rownumcell">' + this.rowNum + '</td>'
      + '<td class="namecell">' + this.tableName + '</td>'
      + '<td class="namecell tableconflictcell">' + this.toConflictDisplayHtmlString() + '</td>'
      + '<td class="tableselectorcell">' + '</td>'
      + '</tr>';
  }

  toConflictDisplayHtmlString() {
    return Array.prototype.reduce.call(this.mappedTableNameList, function (currentStrs, name) {
      if (currentStrs.length === 0) {
        return name;
      }
      return currentStrs + "<br>" + name;
    }, '');
  }

  isTableNameConflict() {
    return this.previousMappingList.length >= 2;
  }
}
