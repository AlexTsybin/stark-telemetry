# Engineering decisions

## Hilt for production dependency injection

Hilt makes the real object graph explicit and conventional for a production Android codebase: application, Activity, ViewModel, repository binding, asset source, JSON configuration, and qualified IO dispatcher. Tests avoid Hilt infrastructure and construct targets directly with fakes, which keeps them fast and focused.

## Pragmatic Clean Architecture in one module

The assessment is too small to justify several Gradle modules, but benefits from clear dependency boundaries. Packages separate `data`, `domain`, `presentation`, `di`, and `ui`. The domain layer is Android-independent, and data DTOs are mapped before crossing the repository boundary. This preserves the useful part of Clean Architecture without build and navigation overhead.

## MVI presentation contract

`DashboardViewModel` exposes a single immutable state stream with Loading, Content, Empty, and Error variants. The UI sends `RetryClicked`; the ViewModel performs the load and reduces the result to a new state. There is no effects channel because this screen has no one-off event, and no reducer dependency because the transition surface is intentionally small.

## One use case with real responsibility

`LoadTelemetrySnapshotUseCase` is not a pass-through. It enforces domain invariants for percentages and non-negative measurements before data reaches presentation. This demonstrates the production pattern while avoiding a use case per trivial field.

## Single snapshot JSON contract

The supplied example has one object at the root and the product asks for a current dashboard, so the asset is parsed as one required snapshot. Accepting both an object and an array would silently broaden the contract and obscure invalid input. If history becomes a requirement, it should be introduced as a distinct repository method and UI flow.

Blank input maps to Empty. Malformed JSON, an array root, `null`, missing required fields, invalid types, and invalid timestamps map to InvalidData. Unknown keys are ignored so additive backend changes remain compatible.

## Fixed landscape with responsive fallback

The phone is physically mounted horizontally, while jumps and falls can produce unwanted sensor rotation. `MainActivity` therefore requests landscape rather than sensor-based orientation. The Compose layout still adapts to window width using a 4/2/1-column grid, so split screen and platforms that ignore requested orientation remain usable.

## Hybrid cockpit visual language

The dark hero anchors bike identity and image; off-white telemetry surfaces maximize daylight legibility. Stark red is reserved for identity and emphasis, while amber communicates diagnostics. Warnings also include icon, message, and code, so meaning is not colour-only. Dynamic colour and system dark mode are intentionally disabled to keep the mounted cockpit predictable.

## Hero image is non-critical data

Coil loads the supplied `image_url`, but loading and error both render a deterministic gradient fallback. Telemetry comes exclusively from the JSON snapshot and remains visible offline. Coil 3.3.0 is used because it is compatible with the template's Kotlin 2.2.10 toolchain; newer Coil 3.6.3 artifacts are compiled against Kotlin 2.4 and cannot be consumed safely without changing the pinned template compiler.

## Deliberate omissions

Navigation, SavedStateHandle, database caching, a live API, Bluetooth state, editing controls, charts, and debug state selectors would add unsupported product behaviour. Alternative UI states are represented by previews and tests instead.
