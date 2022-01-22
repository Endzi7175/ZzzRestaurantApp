import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'restaurant',
        data: { pageTitle: 'restauranteApp.restaurant.home.title' },
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.RestaurantModule),
      },
      {
        path: 'menu',
        data: { pageTitle: 'restauranteApp.menu.home.title' },
        loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule),
      },
      {
        path: 'menu-item',
        data: { pageTitle: 'restauranteApp.menuItem.home.title' },
        loadChildren: () => import('./menu-item/menu-item.module').then(m => m.MenuItemModule),
      },
      {
        path: 'ingredient',
        data: { pageTitle: 'restauranteApp.ingredient.home.title' },
        loadChildren: () => import('./ingredient/ingredient.module').then(m => m.IngredientModule),
      },
      {
        path: 'picture',
        data: { pageTitle: 'restauranteApp.picture.home.title' },
        loadChildren: () => import('./picture/picture.module').then(m => m.PictureModule),
      },
      {
        path: 'restaurant-order',
        data: { pageTitle: 'restauranteApp.restaurantOrder.home.title' },
        loadChildren: () => import('./restaurant-order/restaurant-order.module').then(m => m.RestaurantOrderModule),
      },
      {
        path: 'restaurant-order-item',
        data: { pageTitle: 'restauranteApp.restaurantOrderItem.home.title' },
        loadChildren: () => import('./restaurant-order-item/restaurant-order-item.module').then(m => m.RestaurantOrderItemModule),
      },
      {
        path: 'order-item-notifications-status',
        data: { pageTitle: 'restauranteApp.orderItemNotificationsStatus.home.title' },
        loadChildren: () =>
          import('./order-item-notifications-status/order-item-notifications-status.module').then(
            m => m.OrderItemNotificationsStatusModule
          ),
      },
      {
        path: 'restaurant-table',
        data: { pageTitle: 'restauranteApp.restaurantTable.home.title' },
        loadChildren: () => import('./restaurant-table/restaurant-table.module').then(m => m.RestaurantTableModule),
      },
    ]),
  ],
})
export class EntityRoutingModule {}
