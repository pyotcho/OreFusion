# OreFusion (Paper Plugin)

OreFusion - Paper plugin that adds new enchantment for mining

## 📦 Build from source

1. Clone repository:
   ```bash
   git clone https://github.com/pyotcho/OreFusion.git
   cd OreFusion
   ./gradlew build
2. Create minecraft server with Paper
3. Move .jar file from build/lib to server in folder "plugins"

## ⚙️ Config file

loot:
- spawnChance: 0.05   # Spawn chance for enchantment (5%)

features:
- veinMinerEnabled: true     # Enable vein mining
- autoSmeltingEnabled: true  # Enable smelting some ores

limits:
- maxBlocksPerCycle: 30      # Limit of blocks to destroy by cycle

drop:
- dropIfNoSpace: true        # If no space in inventory other drop will be dropped on the ground

