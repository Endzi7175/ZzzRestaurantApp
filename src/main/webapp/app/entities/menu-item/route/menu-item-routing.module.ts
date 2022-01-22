import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MenuItemComponent } from '../list/menu-item.component';
import { MenuItemDetailComponent } from '../detail/menu-item-detail.component';
import { MenuItemUpdateComponent } from '../update/menu-item-update.component';
import { MenuItemRoutingResolveService } from './menu-item-routing-resolve.service';

const menuItemRoute: Routes = [
  {
    path: '',
    component: MenuItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MenuItemDetailComponent,
    resolve: {
      menuItem: MenuItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MenuItemUpdateComponent,
    resolve: {
      menuItem: MenuItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MenuItemUpdateComponent,
    resolve: {
      menuItem: MenuItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(menuItemRoute)],
  exports: [RouterModule],
})
export class MenuItemRoutingModule {}
