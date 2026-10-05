# Test plan

## Acceptance traceability

| Requirement | Automated coverage | Manual evidence |
| --- | --- | --- |
| Parse the supplied object | Parser exact-payload test | App loads bundled asset |
| Reject invalid roots and fields | Blank, malformed, array, missing-field, wrong-type tests | Error/Empty previews |
| Keep domain values valid | Use-case boundary tests for every validated measurement | Content values reviewed |
| Model MVI transitions | Loading to Content/Empty/Error and retry tests | Loading/Error previews |
| Display the complete rider-facing payload | Compose happy-path test asserts representative data from every section and verifies technical labels are absent | Pixel 6 screenshot and scrolling review |
| Retry from an error | Compose click test verifies `RetryClicked` | Error preview |
| Remain landscape | Manifest configuration | Pixel 6 forced to portrait; Activity remained `SCREEN_ORIENTATION_LANDSCAPE` / `ROTATION_90` |
| Stay useful offline | Image state is independent of dashboard state | Empty URL preview renders hero fallback with telemetry intact |

## Unit scenarios

### Parser

- Exact assessment payload maps to the expected DTO/domain values.
- Unknown fields are ignored.
- Blank input returns no snapshot.
- Malformed JSON, an array root, missing required fields, and wrong types fail as invalid data.

### Repository

- A valid source returns Success.
- Blank source returns Empty.
- Unreadable source maps to SourceUnavailable.
- Parser failures map to InvalidData.
- Coroutine cancellation is not converted into a business error.

### Use case

- A valid snapshot passes unchanged.
- Percentages outside `0..100` fail.
- Negative duration, distance, range, speed, and power fail.
- Missing bike identity fails.

### Presentation

- `3742` seconds formats as `1:02:22`.
- Decimal measurements and UTC timestamp have stable display formats.
- Unknown enum values remain safe and readable.
- ViewModel initially exposes Loading, then reduces Success, Empty, and Failure correctly.
- Retry emits Loading and can recover from Error to Content.

## Compose and device scenarios

- Happy path asserts `73%`, `38 km`, `52.4 hp`, `24.7 km`, `94.1 km/h`, `ENDURO`, firmware, and the warning. It also verifies that `SNAPSHOT` and the raw warning code are not displayed.
- Error Retry dispatches exactly `DashboardIntent.RetryClicked`.
- Content, healthy Content, Loading, Empty, and Error have dedicated Compose previews.
- Manual Pixel 6 API 36 review checks contrast, spacing, scrolling, hero image, fallback behaviour, and absence of overlaps.
- Forced portrait rotation confirms the mounted experience remains landscape.
- Larger font scale should be checked before a production release; the scrollable adaptive grid prevents fixed-screen overflow from becoming a crash or overlap.

## Commands and recorded result

Run from the repository root:

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew lintDebug assembleDebug
```

On 2026-10-05, all three commands passed locally. Both Compose instrumented tests passed on a Pixel 6 API 36 AVD.
