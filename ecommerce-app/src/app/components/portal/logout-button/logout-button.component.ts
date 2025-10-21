import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-logout-button',
  imports: [CommonModule],
  template: `
    <button class="btn btn-outline-danger" (click)="onClick()">
      Cerrar sesión
    </button>
  `
})
export class LogoutButtonComponent {
  @Input() redirectTo: string = '/login';
  constructor(private auth: AuthService) {}
  onClick() { this.auth.logout(this.redirectTo); }
}
