
import { Component, signal, computed, inject } from '@angular/core';
import { RouterLink} from '@angular/router';
import {AuthStore} from '../../store/auth.store';
import {LoginViewModel} from '../../viewModels/login.view-model';
import { ReactiveFormsModule } from '@angular/forms';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {AudioIconComponent} from '../../shared/audio-icon.component/audio-icon.component';
import {AuthLayoutComponent} from '../../shared/auth-layout.component/auth-layout.component'
import {ButtonComponent} from '../../shared/button.component/button.component'
import {LabelComponent} from '../../shared/label.component/label.component'

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink,AudioIconComponent,AuthLayoutComponent,ButtonComponent,LabelComponent],
  templateUrl: './login.component.html',
  styleUrl: '../../../styles.css'
})

  export class LoginComponent {
  private store = inject(AuthStore);
  private loginViewModel = inject(LoginViewModel);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  errorMessage = signal<string | null>(null);


  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  // Accès aux signaux pour le template
  //error = this.store.error;
  loading = this.store.loading;


  onSubmit() {
    this.errorMessage.set(null);

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

      const { email, password } = this.loginForm.value;
      // On appelle le ViewModel avec les données du formulaire
      this.loginViewModel.login(email, password).subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: (err) => {
          this.errorMessage.set(err);
        }
      });

  }

}
