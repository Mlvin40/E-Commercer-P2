import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { LogoutButtonComponent } from '../../portal/logout-button/logout-button.component';

@Component({
  selector: 'app-moderador-home',
  imports: [CommonModule, RouterLink, RouterOutlet, LogoutButtonComponent],
  templateUrl: './moderador-home.component.html',
  styleUrl: './moderador-home.component.scss'
})
export class ModeradorHomeComponent {

}
