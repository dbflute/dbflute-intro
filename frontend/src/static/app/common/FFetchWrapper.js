import { Subject } from 'rxjs';
import { FFetch } from 'ffetch';
import Q from 'q';

function checkStatus(res) {
  if (!res.ok) {
    return res.json()
      .then(err => Promise.reject({
        status: res.status,
        data: err
      }));
  }
  return res;
}

export class FFetchWrapper extends FFetch {

  constructor(...arg) {
    super(...arg);

    this.errorEvents = new Subject();
  }

  get(url, options) {
    return Q(super.get(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res));
  }

  post(url, options) {
    return Q(super.post(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res));
  }

  put(url, options) {
    return Q(super.put(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res));
  }

  del(url, options) {
    return Q(super.del(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res));
  }

  get errors() {
    return this.errorEvents;
  }

  handleResponseError(res) {
    this.errorEvents.next(res);
    return Q.reject(new Error(res));
  }
}

export default FFetchWrapper;