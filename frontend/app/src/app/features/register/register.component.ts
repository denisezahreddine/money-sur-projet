import { Component, inject , signal} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthStore } from '../../store/auth.store';
import { RegisterUseCase } from '../../usecases/register.usecase';
import { AudioIconComponent } from '../../shared/audio-icon.component/audio-icon.component';
import {AuthLayoutComponent} from '../../shared/auth-layout.component/auth-layout.component'
import {ButtonComponent} from '../../shared/button.component/button.component';
import {LabelComponent} from '../../shared/label.component/label.component';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, AudioIconComponent,AuthLayoutComponent,ButtonComponent,LabelComponent],
  templateUrl: './register.component.html',
  styleUrl: '../../../styles.css'
})
export class RegisterComponent {
  private registerUseCase = inject(RegisterUseCase);
  private store = inject(AuthStore);
  private fb = inject(FormBuilder);


  registrationSubmited= false;
  registerForm: FormGroup;
  loading = this.store.loading;
  errorMessage = signal<string | null>(null);


  constructor() {
    this.registerForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/)
      ]],
      confirmPassword: ['', Validators.required],
      typeProfil: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  // Validateur pour vérifier que les deux mots de passe sont identiques
  passwordMatchValidator(g: FormGroup) {
    const pass = g.get('password')?.value;
    const confirm = g.get('confirmPassword')?.value;
    return pass === confirm ? null : { mismatch: true };
  }

  onRegister() {
    this.registrationSubmited = false; // reset AVANT toute tentative
    this.errorMessage.set(null);

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched(); // pour afficher les erreurs
      return;
    }
    if (this.registerForm.valid) {
      const formValue = this.registerForm.value;

      // Extraction du prénom et du nom à partir du nom complet
      const nameParts = formValue.fullName.trim().split(' ');
      const firstName = nameParts[0];
      const lastName = nameParts.length > 1 ? nameParts.slice(1).join(' ') : '.';

      // On prépare l'objet final pour le RegisterUseCase
      const payload = {
        firstName: firstName,
        lastName: lastName,
        email: formValue.email,
        password: formValue.password,
        typeProfil: formValue.typeProfil
      };

      this.registerUseCase.execute(payload).subscribe({
        next: (res: string) => {
          // L'utilisateur doit maintenant aller voir ses mails.
          this.registrationSubmited = true;
          this.errorMessage.set(null);
          console.log(res);

        },
        error: (err) => {
          this.registrationSubmited = false;
          const message = err?.message || 'Échec de l\'inscription';
          this.errorMessage.set(message);
          console.error("Erreur d'inscription", message);
        }
      });
    }
  }
}
