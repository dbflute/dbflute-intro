/**
 * @author cabos
 */
export class UnmappedColumnArea {

  /**
   * Push unmapped column
   */
  push(table, column) {
    let row = new UnmappedColumnRow();
    row.init(this.rows.length + 1, table, column);
    this.rows.push(row);
  }

  /**
   * Activate unmapped column field
   */
  activate() {
    if (document.getElementById("intro_opening") === null) {
      return;
    }

    let unmappedElements = document.getElementById('unmapped-columns');
    unmappedElements.style.display = 'inline';
    const that = this;
    document.getElementById('column-mapping-submit-button').onclick = () => {
      that.postMappingColumn();
    };
  }

  /**
   * Clear unmapped column field
   */
  clear() {
    let tbody = this.getColumnBodyElement();
    this.rows = [];
    tbody.innerHTML = '';

    let unmappedElements = document.getElementById('unmapped-columns');
    unmappedElements.style.display = 'none';
  }

  /**
   * Reflect unmapped column field
   */
  reflect() {
    if (this.rows.length === 0) {
      return;
    }

    this.activate();

    let tbody = this.getColumnBodyElement();
    let that = this;
    Array.prototype.forEach.call(this.rows, (row) => {
      tbody.innerHTML += row.toDisplayHtmlString();
      that.displayColumnSelector(row.tableName, row.oldColumnName);
    });

    if (!this.isColumnNameConflict()) {
      document.querySelector('#unmapped-column-head th.conflictcell').style.display = 'none';
      Array.prototype.forEach.call(document.getElementsByClassName('columnconflictcell'), (cell) => {
        cell.style.display = 'none'
      });
    }
  }

  isColumnNameConflict() {
    return Array.prototype.some.call(this.rows, (row) => {
      return row.isColumnNameConflict();
    });
  }

  /**
   * Display column selector
   */
  displayColumnSelector(tableName, oldColumnName) {
    const columnSelector = this.createColumnSelector(tableName);
    let columnSelectorCell = document.getElementById('columnselectorcell-' + tableName + '-' + oldColumnName);
    columnSelectorCell.innerHTML = columnSelector;
  }

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
  createColumnSelector(tableName) {
    const columnNameList = this.getColumnNameList(tableName);
    const options = Array.prototype.reduce.call(columnNameList, function (currentOptions, name) {
      return currentOptions + '<option>' + name + '</option>';
    }, '');
    return '<select>' + '<option>-</option>' + options + '</select>'
  }

  /**
   * Get column name list by table name
   * @returns {array} column name list
   */
  getColumnNameList(tableName) {
    const trs = document.getElementById(tableName.toLowerCase() + '-body').getElementsByTagName('tr');
    let columnList = [];
    for (var i = 0, max = trs.length; i < max; i++) {
      // TODO check that column name column num is 6
      #if($isAliasDelimiterInDbCommentValid)
      columnList.push(trs[i].getElementsByTagName('td')[7].innerText);
      #else
      columnList.push(trs[i].getElementsByTagName('td')[6].innerText);
      #end
    }
    return columnList;
  }

  /**
   * Get Unmapped column body element
   * @returns {object} Unmapped column body element
   */
  getColumnBodyElement() {
    return document.getElementById('unmapped-column-body');
  }

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
  toDisplayHtmlString() {
    return Array.prototype.reduce.call(this.rows, (currentRows, row) => {
      return currentRows + row.toDisplayHtmlString();
    }, '');
  }

  postMappingColumn() {
    let mappings = Array.prototype.map.call(this.rows, (row) => {
      const tableName = row.tableName;
      const oldColumnName = row.oldColumnName;
      const selectorCellId = 'columnselectorcell-' + tableName + '-' + oldColumnName;
      const newColumnName = document.getElementById(selectorCellId).children[0].value;
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

    mappings = Array.prototype.filter.call(mappings, (mapping) => {
      return mapping.newColumnName !== '-';
    });

    if (mappings.length === 0) {
      return;
    }

    const params = {mappings: mappings};
    const successCallback = () => {
      window.location.reload();
    };
    new ApiClient().postMapping(params, successCallback);
  }
}

class UnmappedColumnRow {

  /**
   * Initialize unmapped column row
   * @param {number} Row number
   * @param {string} Old table name
   */
  init(rowNum, table, column) {
    this.rowNum = rowNum;
    this.tableName = table.tableName;
    this.oldColumnName = column.columnName;

    this.mappedColumnNameList = Array.prototype.map.call(column.mappings, (mapping) => {
      return mapping.newTableName + '.' + mapping.newColumnName;
    });
    this.previousMappingList = Array.prototype.map.call(column.mappings, (mapping) => {
      return mapping.mappingCode;
    });
    this.authorList = Array.prototype.map.call(column.mappings, (mapping) => {
      return mapping.mappingOwner;
    });
  }

  /**
   * Convert unmapped column row to string
   * @returns {string} Unmapped column row
   */
  toDisplayHtmlString() {
    return '<tr id="unmapped-column-row-' + this.oldColumnName + '">'
      + '<td class="rownumcell">' + this.rowNum + '</td>'
      + '<td class="namecell">' + this.tableName + '</td>'
      + '<td class="namecell">' + this.oldColumnName + '</td>'
      + '<td class="namecell columnconflictcell">' + this.toConflictDisplayHtmlString() + '</td>'
      + '<td id="columnselectorcell-' + this.tableName + '-' + this.oldColumnName + '">' + '</td>'
      + '</tr>';
  }

  toConflictDisplayHtmlString() {
    return Array.prototype.reduce.call(this.mappedColumnNameList, (currentStrs, name) => {
      if (currentStrs.length === 0) {
        return name;
      }
      return currentStrs + "<br>" + name;
    }, '');
  }

  isColumnNameConflict() {
    return this.previousMappingList.length >= 2;
  }
}
