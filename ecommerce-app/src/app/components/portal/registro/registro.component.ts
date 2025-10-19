
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { RegistroDto } from '../../../entities/Index';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {
  registro: RegistroDto = { nombre: '', correo: '', password: '' };
  mostrarContrasena = false;
  cargando = false;
  error: string | null = null;
  exito: string | null = null;

  constructor(private usuarios: UsuarioService, private router: Router) {}

  onRegister(form: NgForm) {
    this.error = this.exito = null;
    if (!form.valid) return;

    const payload: RegistroDto = {
      nombre: this.registro.nombre.trim(),
      correo: this.registro.correo.trim().toLowerCase(),
      password: this.registro.password
    };
    if (!payload.nombre || !payload.correo || !payload.password) {
      this.error = 'Completa todos los campos.';
      return;
    }

    this.cargando = true;
    this.usuarios.registrar(payload).subscribe({
      next: () => {
        this.cargando = false;
        this.exito = 'Cuenta creada con éxito. Redirigiendo…';
        setTimeout(() => this.router.navigate(['/login']), 900);
      },
      error: (e) => {
        this.cargando = false;
        this.error = e?.error?.message || 'No se pudo registrar. Intenta de nuevo.';
        console.error(e);
      }
    });
  }
}
