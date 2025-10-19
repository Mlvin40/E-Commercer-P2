import { Component } from '@angular/core';
import { Router} from '@angular/router';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss'
})

export class InicioComponent {

  title = 'revistas-digitales-ui';

  constructor(private router: Router) {}

  goToLogin() {
    this.router.navigate(['/login']); // Redirige al componente de login
  }

  goToRegister() {
    this.router.navigate(['/registro']); // Redirige al componente de registro
  }


}
