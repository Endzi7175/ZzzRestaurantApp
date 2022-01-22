import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderItemNotificationsStatusDetailComponent } from './order-item-notifications-status-detail.component';

describe('OrderItemNotificationsStatus Management Detail Component', () => {
  let comp: OrderItemNotificationsStatusDetailComponent;
  let fixture: ComponentFixture<OrderItemNotificationsStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderItemNotificationsStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderItemNotificationsStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrderItemNotificationsStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderItemNotificationsStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderItemNotificationsStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderItemNotificationsStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
