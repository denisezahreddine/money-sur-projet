import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AudioIcon } from './audio-icon';

describe('AudioIcon', () => {
  let component: AudioIcon;
  let fixture: ComponentFixture<AudioIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AudioIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AudioIcon);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
