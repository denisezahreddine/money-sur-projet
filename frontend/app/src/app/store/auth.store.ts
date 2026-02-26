import {Injectable, signal} from '@angular/core';
import { User } from '../api/models/user.models'

@Injectable({providedIn: 'root'})
export class AuthStore {

  private readonly _currentUser = signal<User | null>(null);
  private readonly _isAuthenticated = signal<boolean>(false);
  private readonly _loading = signal<boolean>(false);
  private readonly _error = signal<string | null>(null);

  readonly currentUser = this._currentUser.asReadonly();
  readonly isAuthenticated = this._isAuthenticated.asReadonly();
  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();

  setLoading(value: boolean) { this._loading.set(value); }


  setLoginSuccess(user: User) {
    this._currentUser.set(user);
    this._isAuthenticated.set(true);
    this._error.set(null);
  }

  setError(message: string| null) { this._error.set(message); }

  setLogout() {
    this._isAuthenticated.set(false);
    this._currentUser.set(null);
  }
}
