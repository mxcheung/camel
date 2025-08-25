
🔑 Key changes

Added fallbackSchemas as a ConcurrentHashMap to store the last successfully fetched schema per subject.

On a successful fetch, the map is updated so that the fallback is always up to date.

On an exception, the service tries to retrieve the last known schema from the fallback map.

If no fallback exists, it throws your custom SchemaRegistryServiceException.


✅ Tests covered:

Happy path – returns schema metadata from registry.

Fallback path – if registry fails, it returns the cached schema.

No fallback – throws SchemaRegistryServiceException when neither registry nor cache works.




✅ What this adds:

shouldUpdateFallbackOnEverySuccess verifies that:

First success populates fallback with schema1.

Next success overwrites fallback with schema2.

On failure, the service falls back to schema2 (the latest).

