import { inject, Injectable } from '@angular/core';
import { RegisterUseCase } from '../usecases/register-usecase';
import { AuthStore } from '../store/auth.store';

@Injectable({ providedIn: 'root' })
export class RegisterViewModel {
  private registerUseCase = inject(RegisterUseCase);
  private store = inject(AuthStore);

  register(userData: any): void {
    // Mapping pour correspondre au backend si nécessaire
    const payload = {
      firstName: userData.firstName,
      lastName: userData.lastName,
      email: userData.email,
      password: userData.password,
      typeProfil: userData.typeProfil || 'SENIOR'
    };

    this.registerUseCase.execute(payload).subscribe();
  }
}
