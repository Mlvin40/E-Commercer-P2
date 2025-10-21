import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { LogoutButtonComponent } from '../../portal/logout-button/logout-button.component';

@Component({
  selector: 'app-comun-home',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet, LogoutButtonComponent],
  templateUrl: './comun-home.component.html',
  styleUrls: ['./comun-home.component.scss']
})
export class ComunHomeComponent {}
