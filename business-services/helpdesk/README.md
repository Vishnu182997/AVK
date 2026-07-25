# Help desk API integration

The module exposes `/api/tickets` through `TicketController`. The repository does not contain a
Spring Security web configuration; the existing authentication boundary must place a `TicketActor`
in the request attribute named `ticketActor`. This deliberately keeps token parsing and role names
out of the feature. The boundary maps its existing authorization result to `ticketManager=true` for
administrators or support users.

Persistence uses the shared JPA configuration and Hibernate schema management used by the parent
project. `Helpdesk_Ticket` is therefore declared by JPA annotations rather than by introducing a new
migration tool.
