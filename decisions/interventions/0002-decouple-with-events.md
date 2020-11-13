# 2. Decouple with events

Date: 2020-11-02

## Status

Accepted

## Context

The Dynamic Framework interventions are the first intervention type onboarded by this system.

### Many new teams

In the current probation programme landscape, we expect

- many new digital teams,
- many changes to existing systems,
- different times when integration happens as teams will have separate priorities.

Specifically, we will have other teams to

- assess risks and needs,
- manage a sentence,
- manage a supervision in the community,
- manage my progress.

All of these teams would have interest in actions carried out in interventions.

### Nature of probation

Additionally, probation

- reacts to what happened in real-life with the supervised service users,
- has various priorities, depending on what happened.

## Decision

The above qualities point towards a decentralised way of integration and notifying each other.

**We will use _domain_ events to decouple** from other production services.

Our domain event payloads will contain:

- transport-specific concerns, e.g. request and tracking IDs, publishing timestamps, etc.
- domain context, e.g. URL on how to find out more, intervention ID, service user ID, etc.
- core domain data, which is required to make sense of the event

The payloads will **not** contain further data by default. **Consumers can request further details via the business API**.

We do not intend to build event sourcing. We will **not** build event logs by default.

**Examples**

For example, our events may be:

- intervention completed
- service user appointment booked
- (many others)

## Consequences

We will build our intervention services to emit known significant domain events by default.

**Benefits**

- The decoupling allows us developing almost all user stories without waiting on endpoints to be finished.
- New and old systems can join as consumers any time.
- We contribute to an event _vocabulary_ for probation, enabling systems with similar responsibilities to
  start publishing the same events.
- Same events from different systems enables us to start using the strangler fig pattern
  by moving the source of those events elsewhere.

**Challenges**

- This architecture is asynchronous and distributed:
  - Request tracking and error handling needs to be built-in to understand what happens in production.
  - It is harder to set up a certain state for pre-production testing as we have to know what events have to happen.
- Lack of atomic transactions:
  - We must not use events that need to be rolled back as a result of a consumer failing to process them.
- Creation, maintenance, and governance of the event contracts is difficult:
  - We need to version the events from the beginning, to avoid backwards-incompatible changes blocking consumers.
  - We need to use event names that are aligned and understood by everyone in HMPPS.
  - We need to document which services emit which events to make discovery and testing easier.

In short, _governance_ will have a much more important role.
