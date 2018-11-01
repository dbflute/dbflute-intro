import {UnmappedTableArea} from 'mapping/unmapped-table'
import {UnmappedColumnArea} from 'mapping/unmapped-column'

/**
 * @author deco
 */
export class StoreClient {
  constructor() {
    // setup from dbflute if decomment pickup exists
    this.embeddedPickup = {}
    this.pickupStore = {}
    this.unmappedTableArea = new UnmappedTableArea();
    this.unmappedColumnArea = new UnmappedColumnArea();
  }

  /**
   * Store pickup json to store
   * @param {Object} pickup - pickup response
   */
  storePickup(pickup) {
    const that = this;
    Array.prototype.forEach.call(pickup["tables"], function (table) {
      that.storePickupTable(table);
      const tableProperty = that.getTablePickup(table['tableName'])
      if (tableProperty.selectTargetHtmlElements().length === 0) {
        that.unmappedTableArea.push(table);
        return;
      }
      Array.prototype.forEach.call(table["columns"], function (column) {
        const columnProperty = that.getColumnPickup(table['tableName'], column['columnName'])
        if (!columnProperty.selectTargetHtmlElement()) {
          that.unmappedColumnArea.push(table, column);
          return;
        }
        that.storePickupColumn(table['tableName'], column);
      });
    });
  }

  /**
   * Store table
   * @param {Object} table - table object of pickup response
   */
  storePickupTable(table) {
    // init if not exists
    const tableName = table['tableName'];
    if (this.pickupStore[tableName] === undefined) {
      this.pickupStore[tableName] = {};
    }

    // return if not have properties
    const properties = table['properties'];
    if (properties.length === 0) {
      return;
    }

    // get database comment
    const tableMap = this.pickupStore[tableName];
    const databaseComment = tableMap['databaseComment'];

    // setup properties to store
    const isConflict = properties.length > 1;
    let isDatabaseCommentConflict = false;
    const decomments = [];
    const previousPieces = [];
    let authors = [];
    let commentVersion = 0;
    Array.prototype.forEach.call(properties, function (property) {
      if (property['databaseComment'] != null && databaseComment != null && !isDatabaseCommentConflict) {
        isDatabaseCommentConflict = DecommentUtil.escapeNewlineCharacter(databaseComment) !== DecommentUtil.escapeNewlineCharacter(property['databaseComment']);
      }
      const decomment = {
        "body": property["decomment"],
        "pieceGitBranch": property["pieceGitBranch"]
      }
      decomments.push(decomment);
      if (property['pieceCode'] != null) {
        previousPieces.push(property['pieceCode']);
      }
      authors = authors.concat(property['authors']);
      if (commentVersion < property['commentVersion']) {
        commentVersion = property['commentVersion'];
      }
    });
    authors = authors.filter(function (x, i, self) {
      return self.indexOf(x) === i;
    });

    tableMap['isConflict'] = isConflict || isDatabaseCommentConflict;
    tableMap['isDatabaseCommentConflict'] = isDatabaseCommentConflict;
    tableMap['decomments'] = decomments;
    tableMap['previousPieces'] = previousPieces;
    tableMap['authors'] = authors;
    tableMap['commentVersion'] = commentVersion;
  }

  /**
   * Store column
   * @param {string} tableDbName - table db name
   * @param {Object} column - column object of pickup response
   */
  storePickupColumn(tableDbName, column) {
    const columnName = column['columnName'];
    // init if not exists
    if (this.pickupStore[tableDbName]['columns'] === undefined) {
      this.pickupStore[tableDbName]['columns'] = {};
    }

    if (this.pickupStore[tableDbName]['columns'][columnName] === undefined) {
      this.pickupStore[tableDbName]['columns'][columnName] = {};
    }

    // return if not have properties
    const properties = column['properties'];
    if (properties.length === 0) {
      return;
    }

    // get database comment
    const columnMap = this.pickupStore[tableDbName]['columns'][columnName];
    const databaseComment = columnMap['databaseComment'];

    // setup properties to store
    const isConflict = properties.length > 1;
    let isDatabaseCommentConflict = false;
    let decomments = [];
    const previousPieces = [];
    let authors = [];
    let commentVersion = 0;
    Array.prototype.forEach.call(properties, function (property) {
      if (property['databaseComment'] != null && databaseComment != null && !isDatabaseCommentConflict) {
        isDatabaseCommentConflict = DecommentUtil.escapeNewlineCharacter(databaseComment) !== DecommentUtil.escapeNewlineCharacter(property['databaseComment']);
      }
      const decomment = {
        "body": property["decomment"],
        "pieceGitBranch": property["pieceGitBranch"]
      }
      decomments.push(decomment);
      if (property['pieceCode'] != null) {
        previousPieces.push(property['pieceCode']);
      }
      authors = authors.concat(property['authors']);
      if (commentVersion < property['commentVersion']) {
        commentVersion = property['commentVersion'];
      }
    });
    authors = authors.filter(function (x, i, self) {
      return self.indexOf(x) === i;
    });

    columnMap['isConflict'] = isConflict || isDatabaseCommentConflict;
    columnMap['isDatabaseCommentConflict'] = isDatabaseCommentConflict;
    columnMap['decomments'] = decomments;
    columnMap['previousPieces'] = previousPieces;
    columnMap['authors'] = authors;
    columnMap['commentVersion'] = commentVersion;
  }

  /**
   * Get decomments
   * @returns {Object} pickup
   */
  getDecomments() {
    const pickup = this.pickupStore;
    const result = [];
    for (let key in pickup) {
      result.push(new PickupTableProperty(key, pickup[key]));
    }
    return result;
  }

  /**
   * Get table pickup
   * @returns {Object} table pickup
   */
  getTablePickup(tableName) {
    return new PickupTableProperty(tableName, this.pickupStore[tableName]);
  }

  /**
   * Get column pickup
   * @returns {Object} column pickup
   */
  getColumnPickup(tableName, columnName) {
    const table = this.getTablePickup(tableName);
    if (!table) {
      return null;
    }
    return new PickupColumnProperty(tableName, columnName, table.columns[columnName])
  }
}
