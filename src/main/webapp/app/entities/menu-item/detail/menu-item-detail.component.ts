import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMenuItem } from '../menu-item.model';

@Component({
  selector: 'jhi-menu-item-detail',
  templateUrl: './menu-item-detail.component.html',
})
export class MenuItemDetailComponent implements OnInit {
  menuItem: IMenuItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItem }) => {
      this.menuItem = menuItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
