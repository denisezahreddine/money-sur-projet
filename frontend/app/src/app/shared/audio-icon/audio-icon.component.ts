import { Component, input } from '@angular/core';

@Component({
  selector: 'app-audio-icon',
  standalone: true,
  templateUrl: './audio-icon.component.html',
  styleUrl: './audio-icon.component.css'
})
export class AudioIconComponent {

  size = input<number>(20);              // taille
  color = input<string>('#3bb273');      // couleur par défaut (vert)

}
