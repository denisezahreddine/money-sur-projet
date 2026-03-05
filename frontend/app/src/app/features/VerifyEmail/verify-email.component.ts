import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthApi } from '../../api/auth.api';
import { AuthStore } from '../../store/auth.store';
import { User } from '../../api/models/user.models';
import {AuthLayoutComponent} from '../../shared/auth-layout.component/auth-layout.component';
import {ButtonComponent} from '../../shared/button.component/button.component';
import { VerifyEmailUseCase } from '../../usecases/verifyEmail.usecase';
import { AudioIconComponent } from '../../shared/audio-icon.component/audio-icon.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule,AuthLayoutComponent,ButtonComponent,AudioIconComponent,RouterLink ],
  templateUrl: './verify-email.component.html',
  styleUrl: './verify-email.component.css'
})
export class VerifyEmailComponent implements OnInit {
  status = signal<'loading' | 'success' | 'error'>('loading');
  errorMessage = signal<string>('');

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private store = inject(AuthStore);
  private verifyEmailUseCase = inject(VerifyEmailUseCase);

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.status.set('error');
      this.errorMessage.set("Token manquant.");
      return;
    }

    this.verifyEmailUseCase.execute(token).subscribe({
      next: (user: User) => {
        this.status.set('success');
        this.store.setLoginSuccess(user);

        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 2000);
      },
      error: (err: Error | any) => {
        this.status.set('error');
        this.errorMessage.set(err.message);

      }
    });
  }
}
