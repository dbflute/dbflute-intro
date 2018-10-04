function PickupTableProperty(tableDbName, json) {
  this.tableDbName = tableDbName;
  this.inputAuthor = null; // use when decomment server mode.
  if (json) {
    this.databaseComment = json.databaseComment;
    this.isDatabaseCommentConflict = json.isDatabaseCommentConflict;
    this.decomments = json.decomments || [];
    this.previousPieces = json.previousPieces;
    this.authors = json.authors;
    this.commentVersion = json.commentVersion;
    this.columns = {};
    var columns = json.columns || {};
    for (var key in columns) {
      this.columns[key] = new PickupColumnProperty(tableDbName, key, columns[key]);
    }
  }
}

PickupTableProperty.prototype = {

  /**
   * Reflect new comment to html
   */
  reflectToHtml: function () {
    if (this.notChangeDisplayComment()) {
      return;
    }
    var author = this.authors.join(',');
    var comment = this.buildDisplayComment() + "\n<span>@author(" + author + ")</span>";
    var that = this;
    Array.prototype.forEach.call(this.selectTargetHtmlElements(), function (element) {
      element.innerHTML = comment;
      if (that.isConflict()) {
        element.className += " commentcellerror"
      }
    });
  },

  /**
   * Convert to dialog title
   * @returns {string} dialog title
   */
  toDialogTitle: function () {
    return this.isConflict() ? this.tableDbName + " ... but now in CONFLICT!" : this.tableDbName;
  },

  /**
   * Check change display comment;
   */
  notChangeDisplayComment: function () {
    return this.decomments.length == 0 && !this.isConflict();
  },

  /**
   * Build display comment to html
   * @return {string} comment
   */
  buildDisplayComment: function () {
    var displayComment = "";
    if (this.isDatabaseCommentConflict) {
      displayComment = this.getEscapedDatabaseComment();
      displayComment = displayComment + '\n(from database comment)\n\n=======\n\n';
    }

    var commentCount = this.decomments.length;
    for (key in this.decomments) {
      var decomment = this.decomments[key];
      displayComment += decomment.body
      if (this.isConflict() && decomment.pieceGitBranch) {
        displayComment += '\n=======' + decomment.pieceGitBranch;
        if (key < commentCount - 1) {
          displayComment += '\n\n'; // show between comments
        }
      } else {
        if (key < commentCount - 1) {
          displayComment += '\n\n=======\n\n'; // show between comments
        }
      }
    }
    return displayComment;
  },

  /**
   * Get escaped database comment
   * @return {string} escaped database comment
   */
  getEscapedDatabaseComment: function () {
    var escapedComment = "";
    escapedComment = DecommentUtil.escapeNewlineCharacter(escapedComment);
    return escapedComment;
  },

  /**
   * Select target html element
   * @return {Array} html elements
   */
  selectTargetHtmlElements: function () {
    var className = this.tableDbName + "_" + "comment";
    var tableElements = document.getElementsByClassName(className);
    var result = [];
    Array.prototype.forEach.call(tableElements, function (element) {
      var target = element.querySelector('pre')
      if (target) {
        result.push(target);
      }
    });
    return result;
  },

  /**
   * Get display html comment
   * @return {string} display html comment
   */
  getDisplayHtmlComment: function () {
    var child = this.selectTargetHtmlElements()[0].childNodes[0];
    if (child != null && child.nodeName === "#text") {
      return child.nodeValue.trim();
    }
    return ""
  },

  /**
   * Check conflict
   * @return {boolean} true: conflict, false: not
   */
  isConflict: function () {
    return this.decomments.length > 1 || this.isDatabaseCommentConflict;
  },

  /**
   * Convert to request params
   * @return {Object} request params
   */
  toRequestParams: function () {
    return {
      'tableName': this.tableDbName,
      'columnName': null,
      'targetType': 'TABLE',
      'decomment': (this.decomments[0] || {}).body,
      'databaseComment': DecommentUtil.escapeNewlineCharacter(this.databaseComment),
      'commentVersion': this.commentVersion || 0,
      'inputAuthor': this.inputAuthor,
      'authors': this.authors || [],
      'previousPieces': this.previousPieces || []
    };
  }
};

function PickupColumnProperty(tableDbName, columnName, json) {
  this.tableDbName = tableDbName;
  this.columnName = columnName;
  this.inputAuthor = null; // use when decomment server mode.
  if (json) {
    this.databaseComment = json['databaseComment'];
    this.isDatabaseCommentConflict = json['isDatabaseCommentConflict'];
    this.decomments = json['decomments'] || [];
    this.previousPieces = json['previousPieces'];
    this.authors = json['authors'];
    this.commentVersion = json['commentVersion'];
    this.previousWholeComment = json['previousWholeComment'];
  }
}

PickupColumnProperty.prototype = {

  /**
   * Reflect new comment to html
   */
  reflectToHtml: function () {
    if (this.notChangeDisplayComment()) {
      return;
    }
    var author = this.authors.join(',');
    var comment = this.buildDisplayComment() + "\n<span>@author(" + author + ")</span>";
    var element = this.selectTargetHtmlElement();
    if (!element) {
      return
    }
    element.innerHTML = comment;
    if (this.isConflict()) {
      element.className += " commentcellerror"
    }
  },

  /**
   * Convert to dialog title
   * @returns {string} dialog title
   */
  toDialogTitle: function () {
    var name = this.tableDbName + "." + this.columnName;
    return this.isConflict() ? name + " ... but now in CONFLICT!" : name;
  },

  /**
   * Check change display comment;
   */
  notChangeDisplayComment: function () {
    return this.decomments.length == 0 && !this.isConflict();
  },

  /**
   * Build display comment to html
   * @return {string} comment
   */
  buildDisplayComment: function () {
    var displayComment = "";
    if (this.isDatabaseCommentConflict) {
      displayComment = this.getEscapedDatabaseComment();
      displayComment = displayComment + '\n(from database comment)\n\n=======\n\n';
    }

    var commentCount = this.decomments.length;
    for (key in this.decomments) {
      var decomment = this.decomments[key];
      displayComment += decomment.body
      if (this.isConflict() && decomment.pieceGitBranch) {
        displayComment += '\n=======' + decomment.pieceGitBranch;
        if (key < commentCount - 1) {
          displayComment += '\n\n'; // show between comments
        }
      } else {
        if (key < commentCount - 1) {
          displayComment += '\n\n=======\n\n'; // show between comments
        }
      }
    }
    return displayComment;
  },

  /**
   * Get escaped database comment
   * @return {string} escaped database comment
   */
  getEscapedDatabaseComment: function () {
    var escapedComment = "";
    escapedComment = DecommentUtil.escapeNewlineCharacter(escapedComment);
    return escapedComment;
  },

  /**
   * Select target html element
   * @return {Object} html element
   */
  selectTargetHtmlElement: function () {
    var columnId = this.tableDbName + "_" + this.columnName;
    columnId = columnId.toLowerCase();
    var columnElement = document.getElementById(columnId);
    if (columnElement === null) {
      return null;
    }
    return columnElement.parentElement.querySelector('.commentcell').querySelector('pre')
  },

  /**
   * Get display html comment
   * @return {string} display html comment
   */
  getDisplayHtmlComment: function () {
    var child = this.selectTargetHtmlElement().childNodes[0];
    if (child != null && child.nodeName === "#text") {
      return child.nodeValue.trim();
    }
    return ""
  },

  /**
   * Check conflict
   * @return {boolean} true: conflict, false: not
   */
  isConflict: function () {
    return this.decomments.length > 1 || this.isDatabaseCommentConflict;
  },

  /**
   * Convert to request params
   * @return {Object} request params
   */
  toRequestParams: function () {
    return {
      'tableName': this.tableDbName,
      'columnName': this.columnName,
      'targetType': 'COLUMN',
      'decomment': (this.decomments[0] || {}).body,
      'databaseComment': DecommentUtil.escapeNewlineCharacter(this.databaseComment),
      'commentVersion': this.commentVersion || 0,
      'inputAuthor': this.inputAuthor,
      'authors': this.authors || [],
      'previousPieces': this.previousPieces || []
    };
  }
};
