import { inject, Injectable } from '@angular/core';
import { LoginUseCase } from '../usecases/login.usecase';
import { User } from '../api/models/user.models';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoginViewModel {
  private loginUseCase = inject(LoginUseCase);

  login(email: string, password: string): Observable<User> {
   return  this.loginUseCase.execute({ email, password }) ;
  }
}
