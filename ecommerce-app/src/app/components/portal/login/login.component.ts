import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth/auth.service';
import { LoginData } from '../../../entities/LoginData';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  correo = '';
  contrasena = '';
  mostrarContrasena = false;
  cargando = false;
  error: string | null = null;

  constructor(private auth: AuthService, private router: Router) {}

  onLogin(form: NgForm) {
    this.error = null;
    if (!form.valid) return;

    const loginData: LoginData = {
      correo: this.correo.trim().toLowerCase(),
      password: this.contrasena
    };

    this.cargando = true;
    this.auth.login(loginData).subscribe({
      next: () => {
        this.cargando = false;
        this.goToHomeByRole(); // ⬅️ redirección por rol
      },
      error: (err) => {
        this.cargando = false;
        this.error = err?.error?.message || 'Credenciales inválidas';
        console.error(err);
      }
    });
  }

  private goToHomeByRole() {
    const role = this.auth.role; // leído del payload del JWT
    const map: Record<string, string> = {
      'ADMIN': '/adminHome',
      'MODERADOR': '/moderadorHome',
      'LOGISTICA': '/logisticaHome',
      'COMUN': '/comunHome',
    };
    this.router.navigate([ map[role ?? 'COMUN'] || '/comunHome' ]);
  }

  irRegistro() { this.router.navigate(['/registro']); }
}
