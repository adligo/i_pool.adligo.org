This is a abstraction of pooled connections which will eventually be used for 
a JDBC DataSource, LDAP Connection pool, SMTP connection pool, and FileConnection pool
and may be extended (WebSocketConnection Pool asdci, HttpConnection pool file uploads asdci)
so that the pools can be done in a common way in the asdci server or other places
so that logging and reporting can be done in a common way.

