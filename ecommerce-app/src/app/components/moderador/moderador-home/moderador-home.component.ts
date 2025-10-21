import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-moderador-home',
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './moderador-home.component.html',
  styleUrl: './moderador-home.component.scss'
})
export class ModeradorHomeComponent {

}
