# 2. Decouple with events

Date: 2020-11-02

## Status

Proposed

## Context

The Dynamic Framework interventions are the first intervention type onboarded by this system.

### Many new teams

In the current probation programme landscape, we expect

- many new digital teams,
- many changes to existing systems,
- different times when integration happens as teams will have separate priorities.

### Nature of probation

Additionally, probation

- reacts to what happened in real-life with the supervised service users,
- has various priorities, depending on what happened.

## Decision

The above qualities point towards a decentralised way of integration and notifying each other.

**We will use _domain_ events to decouple** from other production services.

For example, our events may be:

- intervention completed
- service user appointment booked
- (many others)

----

### Unsure about this one, help please 🙇‍♂️

Some consumers may need to build a local (materialised) view of events that happened in the past, even if
they join the landscape later.

For this use case, **we will also create an event log**, internal to our service.

----

## Consequences

We will build our intervention services to emit known significant domain events by default.

**Benefits**

- The decoupling allows developing almost all user stories without waiting for integrations.
- New and old systems can join as consumers any time.
- Consumers can test their integration with us without running our service.

**Challenges**

- This architecture is asynchronous and distributed:
  - Request tracking and error handling needs to be built-in to understand what happens in production.
  - It is harder to setup a certain state for pre-production testing as we have to know what events have to happen.
- Lack of atomic transactions:
  - We must not use events that need to be rolled back as a result of a consumer failing to process them.
- Creation, maintenance, and governance of the event contracts is difficult:
  - We need to version the events from the beginning, to avoid backwards-incompatible changes blocking consumers.
  - We need to use event names that are aligned and understood by everyone in HMPPS.
  - We need to document which services emit which events to make discovery and testing easier.

In short, _governance_ will have a much more important role.
