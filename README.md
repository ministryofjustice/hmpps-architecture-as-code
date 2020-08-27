![Publish](https://github.com/ministryofjustice/hmpps-architecture-as-code/workflows/Publish/badge.svg)
![Build and validate](https://github.com/ministryofjustice/hmpps-architecture-as-code/workflows/Build%20and%20validate/badge.svg)

# hmpps-architecture-as-code

Modelling architecture in HM Prisons and Probations Service (HMPPS) with the [C4 model][c4] and [Structurizr][structurizr].

## Key outputs

- Holistic representation of all software systems and [containers][c4-abstractions] (applications or data stores) within HMPPS Digital
- Applications annotated with API documentation links are published to the [Published APIs](https://structurizr.com/share/56937/documentation#%2F:Published%20APIs) page
- Interactive graph of model elements and their relationships:
  - [Explore the system interactions](https://structurizr.com/share/56937/explore/graph?softwareSystems=true&view=)
  - [Explore the container (application) interactions](https://structurizr.com/share/56937/explore/graph?containers=true&view=)
  - [Explore how people use systems](https://structurizr.com/share/56937/explore/graph?people=true&softwareSystems=true&view=)
- Views defined in the model are published to the [Diagrams](https://structurizr.com/share/56937/diagrams) page
  - For example, [overview of systems](https://structurizr.com/share/56937/images/system-overview.png) with [legend](https://structurizr.com/share/56937/images/system-overview-key.png)
  - Published diagram images can be embedded in Confluence or GitHub readmes
  - The diagrams will automatically update when they're next changed
- Views defined in the model can be generated locally (see below)

## Workspaces

This repository defines a Structurizr workspace for the HM Prison and Probation Service.

For the model and diagrams, please visit https://structurizr.com/share/56937.

[![Overview](https://static.structurizr.com/workspace/56937/diagrams/system-overview.png)](https://structurizr.com/share/56937/diagrams#system-overview)

![Overview key](https://static.structurizr.com/workspace/56937/diagrams/system-overview-key.png)

## Linking from other repositories

To link to a **live** version of a diagram, insert the following code into your repository's readme:

(Replace `nomiscontainer` with the diagram key in the code)

```markdown
[Container diagram source](https://github.com/ministryofjustice/hmpps-architecture-as-code/search?q=nomiscontainer)

![Container diagram](https://static.structurizr.com/workspace/56937/diagrams/nomiscontainer.png)

![Container diagram legend](https://static.structurizr.com/workspace/56937/diagrams/nomiscontainer-key.png)
```

## Running

The project is built with `gradle`.

| Action | Command |
| --- | --- |
| Build the project | `./gradlew build` |
| Create a local Structurizr workspace JSON file | `./gradlew run` |
| Push to the remote Structurizr workspace | `./gradlew run --args='--push'`<br/>(please see **"Secrets"** section below) |

### :rotating_light: Remote-only changes will be lost

The remote workspace's content is _replaced_ with the content in this repository. Remote-only changes will be **lost**.

### Secrets

The `--push` command can be configured with these environment variables:

| Environment variable | Meaning |
| --- | --- |
| `STRUCTURIZR_API_KEY` | **Required** The API key for the Structurizr API. |
| `STRUCTURIZR_API_SECRET` | **Required** The API secret for the Structurizr API. |
| `STRUCTURIZR_WORKSPACE_ID` | Overrides the default workspace ID. |

Example:
```
STRUCTURIZR_WORKSPACE_ID=12345 \
  STRUCTURIZR_API_KEY=key \
  STRUCTURIZR_API_SECRET=secret \
  ./gradlew run --args='--push'
```

You can view these secrets on the [dashboard](https://structurizr.com/dashboard), after clicking *Show more...* next to
the desired workspace.

## Generating images locally

Requires `plantuml` and `wget` to be installed (e.g. with `brew install plantuml wget`)

```
script/generate_images.sh
```

This command will locally generate all defined workspace diagrams without using the Structurizr web API.


[c4]: https://c4model.com/
[c4-abstractions]: https://c4model.com/#Abstractions
[structurizr]: https://structurizr.com/
