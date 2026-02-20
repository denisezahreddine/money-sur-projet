import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {LoginResponse} from './models/loginResponse.model';



@Injectable({ providedIn: 'root' })
export class AuthApi {

  private http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;


  login(clientCode: string, password: string) {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, {
      clientCode,
      password
    });
  }

  register(name: string, password: string) {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/register`, {
      name,
      password
    })
  }

}
