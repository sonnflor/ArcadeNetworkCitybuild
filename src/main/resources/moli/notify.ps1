param(
    [string]$Title,
    [string]$Message
)

Import-Module BurntToast

New-BurntToastNotification -Text $Title, $Message