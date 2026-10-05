# Engineering decisions

## Hilt for production dependency injection

Hilt keeps production wiring explicit for the application, ViewModel, repository, data source, JSON configuration, and IO dispatcher. Tests construct the same classes directly with fakes.

## Pragmatic Clean Architecture in one module

The assessment is too small for several Gradle modules. Packages still separate `data`, `domain`, `presentation`, `di`, and `ui`; DTOs and Android APIs do not cross into the domain model.

## MVI presentation contract

`DashboardViewModel` exposes one immutable state stream with Loading, Content, Empty, and Error. The only intent is `RetryClicked`; there is no effects channel or reducer dependency.

## One use case with real responsibility

`LoadTelemetrySnapshotUseCase` validates percentages and non-negative measurements before data reaches presentation. No use cases are added for trivial field access.

## Single snapshot JSON contract

The supplied example has one object at the root and the product asks for a current dashboard, so the asset is parsed as one required snapshot. Accepting both an object and an array would silently broaden the contract and obscure invalid input. If history becomes a requirement, it should be introduced as a distinct repository method and UI flow.

Blank input maps to Empty. Malformed JSON, an array root, `null`, missing required fields, invalid types, and invalid timestamps map to InvalidData. Unknown keys are ignored so additive backend changes remain compatible.

## Fixed landscape with responsive fallback

The phone is physically mounted horizontally, while jumps and falls can produce unwanted sensor rotation. `MainActivity` therefore requests landscape rather than sensor-based orientation. The Compose layout still adapts to window width using a 4/2/1-column grid, so split screen and platforms that ignore requested orientation remain usable.

## Hybrid cockpit visual language

The compact dark hero anchors bike identity and image without displacing primary telemetry; off-white telemetry surfaces maximize daylight legibility. Stark red is reserved for identity and emphasis, while amber communicates diagnostics. Warnings include an icon and human-readable message, so meaning is not colour-only. Raw warning and fault codes remain available to logging or a future service screen but are intentionally hidden from the rider-facing overview. Dynamic colour and system dark mode are disabled to keep the mounted UI predictable.

The payload is treated as one frame of an eventually continuous telemetry stream, but the absence of current speed means this is not presented as an at-speed instrument cluster. It is a detailed current overview intended primarily for inspection while stationary. The first viewport prioritizes status and warnings; scrolling is retained rather than sacrificing type size or accessibility on shorter windows.

## Hero image is non-critical data

Coil loads the supplied `image_url`, but loading and error both render a deterministic gradient fallback. Telemetry comes exclusively from the JSON snapshot and remains visible offline. Coil 3.3.0 is used because it is compatible with the template's Kotlin 2.2.10 toolchain; newer Coil 3.6.3 artifacts are compiled against Kotlin 2.4 and cannot be consumed safely without changing the pinned template compiler.

## Deliberate omissions

Navigation, SavedStateHandle, database caching, a live API, Bluetooth state, editing controls, charts, and debug state selectors would add unsupported product behaviour. Alternative UI states are represented by previews and tests instead.
