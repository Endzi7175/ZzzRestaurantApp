import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMenuItem, getMenuItemIdentifier } from '../menu-item.model';

export type EntityResponseType = HttpResponse<IMenuItem>;
export type EntityArrayResponseType = HttpResponse<IMenuItem[]>;

@Injectable({ providedIn: 'root' })
export class MenuItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/menu-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/menu-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(menuItem: IMenuItem): Observable<EntityResponseType> {
    return this.http.post<IMenuItem>(this.resourceUrl, menuItem, { observe: 'response' });
  }

  update(menuItem: IMenuItem): Observable<EntityResponseType> {
    return this.http.put<IMenuItem>(`${this.resourceUrl}/${getMenuItemIdentifier(menuItem) as number}`, menuItem, { observe: 'response' });
  }

  partialUpdate(menuItem: IMenuItem): Observable<EntityResponseType> {
    return this.http.patch<IMenuItem>(`${this.resourceUrl}/${getMenuItemIdentifier(menuItem) as number}`, menuItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMenuItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMenuItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMenuItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addMenuItemToCollectionIfMissing(menuItemCollection: IMenuItem[], ...menuItemsToCheck: (IMenuItem | null | undefined)[]): IMenuItem[] {
    const menuItems: IMenuItem[] = menuItemsToCheck.filter(isPresent);
    if (menuItems.length > 0) {
      const menuItemCollectionIdentifiers = menuItemCollection.map(menuItemItem => getMenuItemIdentifier(menuItemItem)!);
      const menuItemsToAdd = menuItems.filter(menuItemItem => {
        const menuItemIdentifier = getMenuItemIdentifier(menuItemItem);
        if (menuItemIdentifier == null || menuItemCollectionIdentifiers.includes(menuItemIdentifier)) {
          return false;
        }
        menuItemCollectionIdentifiers.push(menuItemIdentifier);
        return true;
      });
      return [...menuItemsToAdd, ...menuItemCollection];
    }
    return menuItemCollection;
  }
}
