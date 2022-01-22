import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOrderItemNotificationsStatus, getOrderItemNotificationsStatusIdentifier } from '../order-item-notifications-status.model';

export type EntityResponseType = HttpResponse<IOrderItemNotificationsStatus>;
export type EntityArrayResponseType = HttpResponse<IOrderItemNotificationsStatus[]>;

@Injectable({ providedIn: 'root' })
export class OrderItemNotificationsStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-item-notifications-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/order-item-notifications-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderItemNotificationsStatus: IOrderItemNotificationsStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItemNotificationsStatus);
    return this.http
      .post<IOrderItemNotificationsStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orderItemNotificationsStatus: IOrderItemNotificationsStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItemNotificationsStatus);
    return this.http
      .put<IOrderItemNotificationsStatus>(
        `${this.resourceUrl}/${getOrderItemNotificationsStatusIdentifier(orderItemNotificationsStatus) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orderItemNotificationsStatus: IOrderItemNotificationsStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItemNotificationsStatus);
    return this.http
      .patch<IOrderItemNotificationsStatus>(
        `${this.resourceUrl}/${getOrderItemNotificationsStatusIdentifier(orderItemNotificationsStatus) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderItemNotificationsStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderItemNotificationsStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderItemNotificationsStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addOrderItemNotificationsStatusToCollectionIfMissing(
    orderItemNotificationsStatusCollection: IOrderItemNotificationsStatus[],
    ...orderItemNotificationsStatusesToCheck: (IOrderItemNotificationsStatus | null | undefined)[]
  ): IOrderItemNotificationsStatus[] {
    const orderItemNotificationsStatuses: IOrderItemNotificationsStatus[] = orderItemNotificationsStatusesToCheck.filter(isPresent);
    if (orderItemNotificationsStatuses.length > 0) {
      const orderItemNotificationsStatusCollectionIdentifiers = orderItemNotificationsStatusCollection.map(
        orderItemNotificationsStatusItem => getOrderItemNotificationsStatusIdentifier(orderItemNotificationsStatusItem)!
      );
      const orderItemNotificationsStatusesToAdd = orderItemNotificationsStatuses.filter(orderItemNotificationsStatusItem => {
        const orderItemNotificationsStatusIdentifier = getOrderItemNotificationsStatusIdentifier(orderItemNotificationsStatusItem);
        if (
          orderItemNotificationsStatusIdentifier == null ||
          orderItemNotificationsStatusCollectionIdentifiers.includes(orderItemNotificationsStatusIdentifier)
        ) {
          return false;
        }
        orderItemNotificationsStatusCollectionIdentifiers.push(orderItemNotificationsStatusIdentifier);
        return true;
      });
      return [...orderItemNotificationsStatusesToAdd, ...orderItemNotificationsStatusCollection];
    }
    return orderItemNotificationsStatusCollection;
  }

  protected convertDateFromClient(orderItemNotificationsStatus: IOrderItemNotificationsStatus): IOrderItemNotificationsStatus {
    return Object.assign({}, orderItemNotificationsStatus, {
      created: orderItemNotificationsStatus.created?.isValid() ? orderItemNotificationsStatus.created.toJSON() : undefined,
      accepted: orderItemNotificationsStatus.accepted?.isValid() ? orderItemNotificationsStatus.accepted.toJSON() : undefined,
      prepared: orderItemNotificationsStatus.prepared?.isValid() ? orderItemNotificationsStatus.prepared.toJSON() : undefined,
      served: orderItemNotificationsStatus.served?.isValid() ? orderItemNotificationsStatus.served.toJSON() : undefined,
      canceled: orderItemNotificationsStatus.canceled?.isValid() ? orderItemNotificationsStatus.canceled.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.accepted = res.body.accepted ? dayjs(res.body.accepted) : undefined;
      res.body.prepared = res.body.prepared ? dayjs(res.body.prepared) : undefined;
      res.body.served = res.body.served ? dayjs(res.body.served) : undefined;
      res.body.canceled = res.body.canceled ? dayjs(res.body.canceled) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((orderItemNotificationsStatus: IOrderItemNotificationsStatus) => {
        orderItemNotificationsStatus.created = orderItemNotificationsStatus.created
          ? dayjs(orderItemNotificationsStatus.created)
          : undefined;
        orderItemNotificationsStatus.accepted = orderItemNotificationsStatus.accepted
          ? dayjs(orderItemNotificationsStatus.accepted)
          : undefined;
        orderItemNotificationsStatus.prepared = orderItemNotificationsStatus.prepared
          ? dayjs(orderItemNotificationsStatus.prepared)
          : undefined;
        orderItemNotificationsStatus.served = orderItemNotificationsStatus.served ? dayjs(orderItemNotificationsStatus.served) : undefined;
        orderItemNotificationsStatus.canceled = orderItemNotificationsStatus.canceled
          ? dayjs(orderItemNotificationsStatus.canceled)
          : undefined;
      });
    }
    return res;
  }
}
