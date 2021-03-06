// @flow strict
import { QueriesActions } from 'views/actions/QueriesActions';
import QueryGenerator from 'views/logic/queries/QueryGenerator';
import ViewStateGenerator from 'views/logic/views/ViewStateGenerator';
import Query from 'views/logic/queries/Query';
import ViewState from 'views/logic/views/ViewState';
import { escape } from 'views/logic/queries/QueryHelper';
import type { ValueActionHandlerWithContext } from './ValueActionHandler';

const UseInNewQueryHandler: ValueActionHandlerWithContext = (queryId: string, field: string, value: string) => {
  const query: Query = QueryGenerator().toBuilder()
    .query({ type: 'elasticsearch', query_string: `${field}:${escape(value)}` })
    .build();
  const state: ViewState = ViewStateGenerator();
  return QueriesActions.create(query, state);
};

export default UseInNewQueryHandler;
