import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ModeracionListaComponent } from '../moderacion-lista/moderacion-lista.component';


@Component({
  selector: 'app-moderador-home',
  imports: [CommonModule, ModeracionListaComponent],
  templateUrl: './moderador-home.component.html',
  styleUrl: './moderador-home.component.scss'
})
export class ModeradorHomeComponent {

}
