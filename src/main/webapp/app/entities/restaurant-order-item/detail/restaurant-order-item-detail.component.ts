import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaurantOrderItem } from '../restaurant-order-item.model';

@Component({
  selector: 'jhi-restaurant-order-item-detail',
  templateUrl: './restaurant-order-item-detail.component.html',
})
export class RestaurantOrderItemDetailComponent implements OnInit {
  restaurantOrderItem: IRestaurantOrderItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantOrderItem }) => {
      this.restaurantOrderItem = restaurantOrderItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
