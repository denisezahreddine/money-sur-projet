import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User } from '../api/models/user.models';


@Injectable({ providedIn: 'root' })
export class AuthApi {
  private http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  login(credentials: any) {
    return this.http.post(`${this.baseUrl}/auth/login`, credentials, {
      withCredentials: true
    });
  }

  register(userData: any) {
    return this.http.post(`${this.baseUrl}/auth/register`, userData, {
      withCredentials: true
    });
  }

  verifyEmail(token: string) {
    return this.http.get<User>(`${this.baseUrl}/auth/verify-email`, {
      params: { token },
      withCredentials: true
    });
  }
}
