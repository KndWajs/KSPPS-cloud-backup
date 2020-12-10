$host.ui.RawUI.WindowTitle = "TWORZENIE KOPII BEZPIECZEŃSTWA"

$time = Get-Date -Format "yyyyMMdd_HHmmss"
$mypath = split-path -parent $MyInvocation.MyCommand.Definition

$7zipPath = "$env:ProgramFiles\7-Zip\7z.exe"
$Source = "C:\Kopie_bezpieczenstwa\KOPIA.bak"
$TargetPath = "C:\Kopie_bezpieczenstwa\"
$Target = $TargetPath + "KSPPS_$time.zip"

# Write-Output "Prosze wlozyc pendrivea"
# Write-Output ""
# Write-Output "po tych czynnosciach nalezy nacisnac dowolny klawisz"
# If (!(Test-Path D:)) { Write-Output "Nie odnaleziono pendrivea, plik zostanie zapisany jedynie na dysku twardym"};
# pause

$host.ui.RawUI.WindowTitle =  "krok 1/3 - WYKONUJE KOPIE BEZPIECZENSTWA PROGRAMU KSPPS"

Write-Output ""
Write-Output "krok 1/3 - tworzenie kopi bezpieczenstwa(ok 1 minuty)..."
Write-Output ""

try { 
#& "C:\Program Files (x86)\Firebird\Firebird_2_5\bin\gbak.exe" -b -user SYSDBA -pass masterkey LOCALHOST:C:\KS\KS-PPS\BAZA\KSPPS.FDB $Source
 }
catch { "An error occurred." }

$host.ui.RawUI.WindowTitle = "krok 2/3 - KOMPRESJA PLIK KOPII BEZPIECZENSTWA PROGRAMU KSPPS"

Write-Output ""
Write-Output "krok 2/3 - kompresowanie kopi bezp. i zapisywanie na dysku komputera(ok. 2 minuty)..."
Write-Output ""

if (-not (Test-Path -Path $7zipPath -PathType Leaf)) {
    throw "7 zip file '$7zipPath' not found"
}

Set-Alias 7zip $7zipPath

7zip a -mx=1 $Target $Source

$host.ui.RawUI.WindowTitle = "krok 3/3 - ZAPISYWANIE KOPII BEZPIECZENSTWA PROGRAMU KSPPS"
Write-Output ""
Write-Output "krok 3/3 - zapisywanie kopii w chmurze..."
Write-Output ""

java -jar "$mypath\pps-cloud-backup-1.0-SNAPSHOT-jar-with-dependencies.jar" "$TargetPath"

Write-Output ""
Write-Output "Dziekuje, to wszystko"
Write-Output ""

# Stop-Computer -ComputerName localhost

