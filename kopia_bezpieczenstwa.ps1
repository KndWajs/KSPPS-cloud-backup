$host.ui.RawUI.WindowTitle = "TWORZENIE KOPII BEZPIECZEŃSTWA"

$time = Get-Date -Format "yyyyMMdd_HHmmss"
$mypath = split-path -parent $MyInvocation.MyCommand.Definition

Write-Output "Prosze wlozyc pendrivea"
Write-Output ""
Write-Output "po tych czynnosciach nalezy nacisnac dowolny klawisz"

If (!(Test-Path D:)) { Write-Output "Nie odnaleziono pendrivea, plik zostanie zapisany jedynie na dysku twardym"};
pause


$host.ui.RawUI.WindowTitle =  "WYKONUJE KOPIE BEZPIECZENSTWA PROGRAMU KSPPS"

Write-Output "krok 1/3 - tworzenie kopi bezpieczenstwa(ok 1 minuty)..."

try { 
& "C:\Program Files (x86)\Firebird\Firebird_2_5\bin\gbak.exe" -b -user SYSDBA -pass masterkey LOCALHOST:C:\KS\KS-PPS\BAZA\KSPPS.FDB C:\Kopie_bezpieczenstwa\KOPIA.bak
 }
catch { "An error occurred." }



$host.ui.RawUI.WindowTitle = "KOMPRESJA PLIK KOPII BEZPIECZENSTWA PROGRAMU KSPPS"

Write-Output "krok 2/3 - kompresowanie kopi bezp. i zapisywanie na dysku komputera(ok 0,5 minuty)..."
Write-Output "nazwa kopii - C:\Kopie_bezpieczenstwa\KSPPS_$time.zip"

& "C:\Program Files\7-zip\7zG.exe" a -mx1 "C:\Kopie_bezpieczenstwa\KSPPS_$time.zip" "C:\Kopie_bezpieczenstwa\KOPIA.bak";
cd C:

Write-Output "krok 3/3 - zapisywanie kopii na PENIE(ok 1 minuty)..."
Start-Sleep -s 1.5
copy "C:\Kopie_bezpieczenstwa\KSPPS_$time.zip" "D:\"


java -jar "$mypath\pps-cloud-backup-1.0-SNAPSHOT-jar-with-dependencies.jar"

Write-Output ""
Write-Output "Dziekuje, to wszystko"
Write-Output ""
Write-Output ""
Write-Output ""
pause