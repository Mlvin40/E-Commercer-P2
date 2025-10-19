import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogisticaHomeComponent } from './logistica-home.component';

describe('LogisticaHomeComponent', () => {
  let component: LogisticaHomeComponent;
  let fixture: ComponentFixture<LogisticaHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogisticaHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogisticaHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
