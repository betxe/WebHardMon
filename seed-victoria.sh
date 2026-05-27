#!/bin/bash

VM_URL="http://localhost:8428/api/v1/import/prometheus"

echo "Insertando datos de prueba en VictoriaMetrics..."

PROCESADORES=("Intel_i5_1135G7" "Intel_i7_1165G7" "AMD_Ryzen_5_5600U" "AMD_Ryzen_7_5800U" "Apple_M1")
RAM_TOTAL_OPCIONES=(8 16 32 64)
STORAGE_TOTAL_OPCIONES=(256 512 1024 2048)

for i in {1..100}
do
  TIMESTAMP=$(($(date +%s) * 1000))

  EMPRESA_ID=$(( (i % 3) + 1 ))
  PORTATIL_ID="PC-$EMPRESA_ID-$i"

  CPU=$(( RANDOM % 100 ))
  RAM=$(( 30 + RANDOM % 70 ))
  DISK=$(( 20 + RANDOM % 80 ))
  TEMP=$(( 35 + RANDOM % 55 ))
  BATTERY=$(( RANDOM % 100 ))
  STRESS_SCORE=$(( (CPU * 35 + RAM * 25 + DISK * 20 + TEMP * 20) / 100 ))

  RAM_TOTAL=${RAM_TOTAL_OPCIONES[$(( i % ${#RAM_TOTAL_OPCIONES[@]} ))]}
  STORAGE_TOTAL=${STORAGE_TOTAL_OPCIONES[$(( i % ${#STORAGE_TOTAL_OPCIONES[@]} ))]}
  PROCESADOR=${PROCESADORES[$(( i % ${#PROCESADORES[@]} ))]}

  cat <<EOD | curl -s -X POST "$VM_URL" --data-binary @-
cpu_usage_percent{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $CPU $TIMESTAMP
ram_usage_percent{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $RAM $TIMESTAMP
disk_usage_percent{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $DISK $TIMESTAMP
temperature_celsius{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $TEMP $TIMESTAMP
battery_percent{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $BATTERY $TIMESTAMP
stress_score{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $STRESS_SCORE $TIMESTAMP
ram_total_gb{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $RAM_TOTAL $TIMESTAMP
storage_total_gb{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID"} $STORAGE_TOTAL $TIMESTAMP
processor_info{empresa="$EMPRESA_ID",portatil="$PORTATIL_ID",procesador="$PROCESADOR",ram="$RAM_TOTAL",storage="$STORAGE_TOTAL"} 1 $TIMESTAMP
EOD

  sleep 1
done

echo "Datos insertados correctamente."
