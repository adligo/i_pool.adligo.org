This is a abstraction of pooled connections which will eventually be used for 
a JDBC DataSource, LDAP Connection pool and FileConnection pool
and may be extended (WebSocketConnection Pool asdci, HttpConnection pool file uploads asdci)
so that the pools can be done in a common way in the asdci server
so that logging and reporting can be done in a consistant way.

This also includes a LDAP api, for making ldap commands easier for asdci 
and provides a binary streaming ability for large ldap files
(I will also add a ldap schema here for large files when INIA gets back to me with a 2nd OID).
