/**
 * @author deco
 */
class ApiClient {
  /**
   * Fetch decomment viertual pickup.
   * @param {function} successCallback
   */
  fetchDecomment(successCallback) {
    const path = '/document/decomment/' + DecommentUtil.getSchemaName() + '/pickup';
    const method = 'GET';
    const errorCallback = function () {
      alert('Error!! cannot save decomment.');
    }
    this.request(path, method, null, successCallback, errorCallback);
  }

  /**
   * Request save decomment
   * @param {Object} params - Request parameters
   * @param {function} successCallback
   */
  saveDecomment(params, successCallback) {
    const path = '/document/decomment/' + DecommentUtil.getSchemaName() + '/save';
    const method = 'POST';
    const errorCallback = function () {
      alert('Error!! cannot save new column comment.');
    }
    this.request(path, method, params, successCallback, errorCallback);
  }

  /**
   * Post mapping table.
   * @param {Object} params - Request parameters
   * @param {function} successCallback
   */
  postMapping(params, successCallback) {
    const path = '/document/decomment/' + DecommentUtil.getSchemaName() + '/mapping';
    const method = 'POST';
    const errorCallback = function () {
      alert('Error!! cannot save new table mapping.');
    }
    this.request(path, method, params, successCallback, errorCallback);
  }

  /**
   * Request to intro server
   * @param {string} path
   * @param {string} method
   * @param {object} params
   * @param {function} successCallback
   * @param {function} errorCallback
   */
  request(path, method, params, successCallback, errorCallback) {
    const http = new XMLHttpRequest();
    http.withCredentials = true;
    const url = DecommentUtil.getHostName() + "/api" + path;
    http.open(method, url, true);

    http.onreadystatechange = function () {
      if (http.readyState === 4) {
        if (http.status === 200) {
          successCallback(http.response)
        } else {
          errorCallback()
        }
      }
    };
    if (params != null) {
      http.send(JSON.stringify(params, null, "  "));
    } else {
      http.send();
    }
  }
}
