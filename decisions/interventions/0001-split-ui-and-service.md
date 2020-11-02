# 1. Split user interface and service by default

Date: 2020-11-02

## Status

Proposed

## Context

The [current HMPPS Digital tech radar][radar] elects Java, Kotlin and node.js as languages in _adopt_. It also
mentions Ruby (for London); however, the current direction is to reduce regional differences as it is difficult
to hire developers who are comfortable and happy working with multiple languages.

In short, HMPPS Digital is heading towards using Java, Kotlin and node.js as their primary choices.

The department's talent pool reflects this split; there are:

- many node.js focussed frontend specialists,
- many Java/Kotlin focussed backend specialists,
- and a few full-stack developers.

### Team

The current interventions team is mostly on a timed contract with a managed service provider.
When their contract runs out, we need to be able to backfill developers.

Consequently, we need to choose a technology stack that most other developers in HMPPS already know.

## Decision

To reflect the available talent pool in our department, we will **split the interventions service into a UI
and a service component**.

## Consequences

**Benefits**

- The pool of talent who can join our team from HMPPS is larger.
- There are reusable best practices already available from DPS.

**Challenges**

- Contracts between the UI component and the service component will take a while to stabilise, creating churn in the beginning.
- Expectations between the parts have to be clear to avoid blocking others or taking up time. [Consumer-driven contract testing][cdct] can mitigate.

In short, we gain _maintainability_ benefits by trading off initial speed.

[radar]: https://ministryofjustice.github.io/hmpps-digital-tech-radar/docs/index.html
[cdct]: https://www.thoughtworks.com/radar/techniques/consumer-driven-contract-testing
