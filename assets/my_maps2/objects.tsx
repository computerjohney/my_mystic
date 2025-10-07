<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="objects" tilewidth="80" tileheight="112" tilecount="3" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/house/house.png" width="80" height="112"/>
 </tile>
 <tile id="1" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/oak_tree/oak_tree.png" width="41" height="63"/>
 </tile>
 <tile id="2" type="GameObject">
  <properties>
   <property name="animation" value="IDLE"/>
   <property name="animationSpeed" type="float" value="1"/>
   <property name="atlasAsset" value="OBJECTS"/>
   <property name="speed" type="float" value="3"/>
  </properties>
  <image source="objects/player/player.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="18.1818" width="10.8182" height="5.45455">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
