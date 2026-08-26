# PowerShell Script to Sync Seperate_roles into Master Frontend & backend-java
$base = $PSScriptRoot

Write-Host "Syncing Frontend roles into master Frontend folder..." -ForegroundColor Cyan

# 1. Sync Frontend Auth
Copy-Item "$base\Seperate_roles\frontend-auth\src\pages\*" "$base\Frontend\src\pages\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-auth\src\services\*" "$base\Frontend\src\services\" -Force -Recurse

# 2. Sync Frontend Form Components & Styles
Copy-Item "$base\Seperate_roles\frontend-form-components\src\pages\*" "$base\Frontend\src\pages\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-form-components\src\components\*" "$base\Frontend\src\components\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-form-components\src\App.css" "$base\Frontend\src\App.css" -Force
Copy-Item "$base\Seperate_roles\frontend-form-components\src\index.css" "$base\Frontend\src\index.css" -Force

# 3. Sync Frontend Core UI
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\pages\*" "$base\Frontend\src\pages\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\components\*" "$base\Frontend\src\components\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\context\*" "$base\Frontend\src\context\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\layouts\*" "$base\Frontend\src\layouts\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\data\*" "$base\Frontend\src\data\" -Force -Recurse
Copy-Item "$base\Seperate_roles\frontend-core-ui\src\App.jsx" "$base\Frontend\src\App.jsx" -Force

Write-Host "Syncing Backend Java roles into master backend-java folder..." -ForegroundColor Cyan

# 4. Sync Backend Java Auth & Wishlist
Copy-Item "$base\Seperate_roles\backend-java-auth-wishlist\src\main\java\com\yummiee\model\*" "$base\backend-java\src\main\java\com\yummiee\model\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-auth-wishlist\src\main\java\com\yummiee\repository\*" "$base\backend-java\src\main\java\com\yummiee\repository\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-auth-wishlist\src\main\java\com\yummiee\service\*" "$base\backend-java\src\main\java\com\yummiee\service\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-auth-wishlist\src\main\java\com\yummiee\controller\*" "$base\backend-java\src\main\java\com\yummiee\controller\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-auth-wishlist\src\main\java\com\yummiee\config\*" "$base\backend-java\src\main\java\com\yummiee\config\" -Force -Recurse

# 5. Sync Backend Java Recipe & Shopping List
Copy-Item "$base\Seperate_roles\backend-java-recipe-shopping\src\main\java\com\yummiee\model\*" "$base\backend-java\src\main\java\com\yummiee\model\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-recipe-shopping\src\main\java\com\yummiee\repository\*" "$base\backend-java\src\main\java\com\yummiee\repository\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-recipe-shopping\src\main\java\com\yummiee\service\*" "$base\backend-java\src\main\java\com\yummiee\service\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-recipe-shopping\src\main\java\com\yummiee\controller\*" "$base\backend-java\src\main\java\com\yummiee\controller\" -Force -Recurse
Copy-Item "$base\Seperate_roles\backend-java-recipe-shopping\src\main\java\com\yummiee\dto\*" "$base\backend-java\src\main\java\com\yummiee\dto\" -Force -Recurse

Write-Host "Sync Complete! Full application updated successfully." -ForegroundColor Green
