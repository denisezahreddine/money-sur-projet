import { Component, inject } from '@angular/core'; // Import essentiel
import { CommonModule } from '@angular/common'; // Pour le HTML
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms'; // Pour les formulaires
import { AuthStore } from '../../store/auth.store';
import { RegisterUseCase } from '../../usecases/register-usecase';



@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private registerUseCase = inject(RegisterUseCase);
  private store = inject(AuthStore);

  registerForm: FormGroup;
  loading = this.store.loading;
  error = this.store.error;

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      typeProfil: ['SENIOR'] // Valeur par défaut
    });
  }

  onRegister() {
    if (this.registerForm.valid) {
      this.registerUseCase.execute(this.registerForm.value);
    }
  }
}
