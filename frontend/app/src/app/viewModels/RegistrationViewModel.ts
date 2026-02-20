import {Injectable, inject} from '@angular/core';
import {RegisterUsecase} from '../usecases/register-usecase.service';


@Injectable()
export class RegistrationViewModel {
  private registrationUsecase = inject(RegisterUsecase);

  register(clientCode: string, password: string): void {
    this.registrationUsecase.execute(clientCode, password)
  }
}
