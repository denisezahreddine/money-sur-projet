import {Component, input, OnInit, output} from '@angular/core';

@Component({
  selector: 'app-password-component',
  imports: [],
  templateUrl: './password-component.html',
  styleUrls: ['./password-component.css'],
})
export class PasswordComponent implements OnInit {
// On reçoit la valeur actuelle du mot de passe (signal-based input)
  value = input.required<string>();
  maxLength = input<number>(6);

  // On émet les changements vers le parent
  valueChange = output<string>();

  numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0,-1,-2];

  shuffleNumbers() {
   /* for (let i = this.numbers.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [this.numbers[i], this.numbers[j]] = [this.numbers[j], this.numbers[i]];
    }*/
    for (let i =0; i < this.numbers.length ; i++) {
      const j = Math.floor(Math.random() * (i ));
      [this.numbers[i], this.numbers[j]] = [this.numbers[j], this.numbers[i]];
    }
  }

  ngOnInit() {
    this.shuffleNumbers();
  }

  addNumber(num: number) {
    if(num>=0) {
      if (this.value().length < this.maxLength()) {
        this.valueChange.emit(this.value() + num.toString());
      }
    }
  }

  deleteLast() {
    this.valueChange.emit(this.value().slice(0, -1));
  }

}
