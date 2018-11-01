import ApiClient from 'api-client'

/**
 * @author deco
 * @author cabos
 */
class Decomment {
  constructor() {
    this.storeClient = new StoreClient()
  }

  /**
   * init
   */
  init() {
    if (document.getElementById("intro_opening") !== null) {
      this.fetchVirtualPickup();
      this.setupKeyDownEvent();
      this.showUserNameInputIfNeed();
    } else {
      this.reflectEmbeddedPickup();
    }
  }

  /**
   * Fetch virtual pickup from server
   */
  fetchVirtualPickup() {
    const that = this;
    new ApiClient().fetchDecomment(function (response) {
      const decomment = JSON.parse(response);
      that.reflectPickup(decomment);
      that.setupSaveDecomment();
    })
  }

  /**
   * Show user name input when decomment server mode.
   */
  showUserNameInputIfNeed() {
    if (DecommentUtil.isDecommentServer() && this.isUserNameEmpty()) {
      document.querySelector('#decomment-user-name').style.display = 'block';
    }
  }

  hideUserNameInput() {
    document.querySelector('#decomment-user-name').style.display = 'none';
  }

  /**
   * Reflect embedded pick
   */
  reflectEmbeddedPickup() {
    this.reflectPickup(this.storeClient.embeddedPickup);
  }

  /**
   * Reflect pickup
   * @param {Object} decomment - Decomment object
   */
  reflectPickup(decomment) {
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
  }

  /**
   * Setup save decomment event
   * @param {string} table - Table name
   */
  setupSaveDecomment(schemeName) {
    this.setupSaveTableComment();
    this.setupSaveColumnComment();
  }

  /**
   * Setup save table comment event
   */
  setupSaveTableComment() {
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
  }

  /**
   * Setup save table comment click event to html
   * @param {string} tableName - Table name
   * @param {Object} commentElement - Comment element
   */
  setupSaveTableClickEvent(tableName, commentElement) {
    var tableProperty = this.storeClient.getTablePickup(tableName) || new PickupTableProperty(tableName, null)
    commentElement.onclick = this.createSaveDecommentFunction(tableProperty);
  }

  /**
   * Setup save column comment event
   */
  setupSaveColumnComment() {
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
  }

  /**
   * Create save decomment function
   * param {object} commentProperty - Comment property
   * returns {function} Save decomment function
   */
  createSaveDecommentFunction(commentProperty) {
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
  }

  /**
   * Get user name. and save to cookie if not saved.
   */
  getUserName() {
    var savedUserName = this.getCookie('decomment-user-name');
    if (savedUserName) {
      return savedUserName;
    }
    var inputUserName = document.querySelector('#decomment-user-name').value;
    if (inputUserName && inputUserName.length > 0) {
      document.cookie = "decomment-user-name=" + inputUserName + ";expires=2147483647";
    }
    return inputUserName;
  }

  /**
   * Check user name is inputed.
   */
  isUserNameEmpty() {
    return this.getCookie('decomment-user-name') === null && this.getUserName().length === 0;
  }

  /**
   * Get cookie from key
   */
  getCookie(key) {
    var cookies = document.cookie.split('; ');
    for (var i = 0; i < cookies.length; i++) {
      var c = cookies[i].split('=');
      if (c[0] === key) {
        return c[1];
      }
    }
    return null;
  }

  /**
   * Setup key down event
   */
  setupKeyDownEvent() {
    document.onkeydown = function (e) {
      // ESC
      if (e.keyCode == 27) {
        var modalElement = document.getElementById("decomment-modal");
        modalElement.style.display = "none";
      }
    }
  }

  /**
   * Get table name
   * @param {Object} table - Table element
   * @returns {string} table name
   */
  getTableNameFromElement(table) {
    var previousElement = table.previousElementSibling;
    return previousElement.value;
  }

  /**
   * Check still conflicting
   * @param {string} decomment - decomment
   * @returns {Boolean} is still conflicting
   */
  isStillConflicting(decomment) {
    return decomment.includes('=======');
  }

  /**
   * Check new decomment and previous comment is different
   * @param {string} tableName - Table name
   * @param {string} columnName - Column name
   * @param {string} decomment - decomment
   * @returns {Boolean} is changed
   */
  isCommentChanged(previousWholeComment, decomment) {
    if (previousWholeComment) {
      return previousWholeComment.trim() !== decomment.trim();
    }
    return decomment.trim().length != 0;
  }
}

new Decomment().init();
