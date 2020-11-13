# 1. Split user and business interfaces by default

Date: 2020-11-02

## Status

Accepted

## Context

### Need for sustainable services

The Ministry of Justice 2022 Digital Strategy defines "Building sustainable services" as a priority.

As the domain model of HM Prisons and Probation Service (HMPPS) is big, we aim to build sustainable services
around [bounded contexts][bounded-context], making it necessary to integrate with other contexts.

### Domain and business logic

Historically, many systems built in HMPPS did not expose an API for the business logic and/or were managed by third
parties, leading to difficult integration: we see business APIs beneficial to have from the start.

Existing APIs that are thin layers over a database (entity services) are easy to write but hard to use as it pushes
the need to handle business logic back to the clients.

We have seen services with a frontend/backend split where frontend clients accumulated business logic as the
backend service fell back to provide a CRUD entity service over the database; this is not sustainable.

_Sam Newman_ writes in _Monolith to Microservices_ (O'Reilly, 2019):

> Fundamentally, in a system that consists of multiple independent services, there has to be some interaction between
> the participants. In a microservice architecture, _domain coupling_ is the result—the interactions between services
> model the interactions in our real domain.

We want to aim for domain-level coupling to avoid business logic drifting into current and future clients.

### Languages in "adopt"

HMPPS Digital is heading towards using Java/Kotlin and node.js as their primary language choices.

The [current HMPPS Digital tech radar][radar] elects the mentioned languages in _adopt_. It also mentions Ruby (for London);
however, the current direction is to reduce regional differences as it is difficult to hire developers who are comfortable
and happy working with multiple languages.

The department's talent pool reflects this split; there are:

- many node-focussed frontend specialists,
- many Java/Kotlin-focussed backend specialists,
- a few full-stack developers.

## Decision

We will **create standalone business interfaces (business/domain APIs) by default**.

We will **split the user interface** from the API's component to enforce the use of the business API by default
and better utilise the talent we have.

We realise this is an optimisation to build durable domain-coupled systems at the cost of some team autonomy.

## Consequences

**Benefits**

- The APIs we create can be used by other teams if they wish to hook into our business processes.
- There are reusable best practices already available from DPS for separate API components.
- The pool of talent who can join our team from HMPPS is larger.

**Challenges**

- We need to be more careful on what services we create and what API they have. This increases _governance_,
  which we must keep lightweight.
- Contracts between the UI and the service components will take a while to stabilise, creating churn in the beginning.
- Expectations between the parts have to be clear to avoid blocking others or taking up time. [Consumer-driven contract testing][cdct] can mitigate.

In short, we gain _sustainability_ benefits by trading off _governance_ around the API contracts.

[radar]: https://ministryofjustice.github.io/hmpps-digital-tech-radar/docs/index.html
[cdct]: https://www.thoughtworks.com/radar/techniques/consumer-driven-contract-testing
[bounded-context]: https://martinfowler.com/bliki/BoundedContext.html
