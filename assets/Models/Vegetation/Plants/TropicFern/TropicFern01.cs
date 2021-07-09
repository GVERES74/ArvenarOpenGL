
singleton TSShapeConstructor(TropicFern01DAE)
{
   baseShape = "./TropicFern01.DAE";
};

function TropicFern01DAE::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
